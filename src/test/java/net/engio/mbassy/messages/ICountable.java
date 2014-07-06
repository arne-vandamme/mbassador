package net.engio.mbassy.messages;

/**
 * Interface analogous to IMessage. Exists to test more complex class/interface hierarchies
 *
 * @author bennidi
 *         Date: 5/24/13
 */
public interface ICountable<T> {

    void reset();

    void handled(T listener);

    int getTimesHandled(T listener);
}
