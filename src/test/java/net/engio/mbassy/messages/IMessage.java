package net.engio.mbassy.messages;

/**
 *
 * @author bennidi
 *         Date: 5/24/13
 */
public interface IMessage<T> {

    void reset();

    void handled(T listener);

    int getTimesHandled(T listener);

}
