package net.engio.mbassy;

import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.bus.config.BusConfiguration;
import net.engio.mbassy.common.MessageBusTest;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.messages.InstanceMessage;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;

/**
 * Tests the defined priority of listeners and handlers, and the runtime priority of listeners.
 */
public class PriorityTest extends MessageBusTest
{
	static class ListenerOne
	{
		@Handler
		void handle( InstanceMessage message ) {
			message.handled( "one-one" );
		}

		@Handler(priority = Integer.MAX_VALUE - 1)
		void handleOther( InstanceMessage message ) {
			message.handled( "one-two" );
		}
	}

	static class ListenerTwo
	{
		private final String name;

		ListenerTwo( String name ) {
			this.name = name;
		}

		@Handler
		void handle( InstanceMessage message ) {
			message.handled( name );
		}
	}

	static class ListenerThree
	{
		@Handler(priority = Integer.MAX_VALUE)
		void handle( InstanceMessage message ) {
			message.handled( "three-one" );
		}
	}

	@Test
	public void noPrioritiesMeansReverseSubscribeOrder() {
		MBassador bus = getBus( BusConfiguration.Default() );
		bus.subscribe( new ListenerTwo( "two-one" ) );
		bus.subscribe( new ListenerTwo( "two-two" ) );

		assertCallOrder( bus, "two-two", "two-one" );
	}

	@Test
	public void handlerPrioritiesFirst() {
		MBassador bus = getBus( BusConfiguration.Default() );
		bus.subscribe( new ListenerOne() );
		bus.subscribe( new ListenerOne() );
		bus.subscribe( new ListenerTwo( "two-one" ) );
		bus.subscribe( new ListenerThree() );

		assertCallOrder( bus, "three-one", "one-two", "one-one", "two-one" );
	}

	private void assertCallOrder( MBassador bus, Object... expectedHandlers ) {
		InstanceMessage message = new InstanceMessage();
		bus.publish( message );

		List<Object> handlers = message.getHandledList();

		assertArrayEquals( expectedHandlers, handlers.toArray( new Object[handlers.size()] ) );
	}
}
