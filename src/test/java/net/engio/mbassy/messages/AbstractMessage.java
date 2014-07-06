package net.engio.mbassy.messages;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author bennidi
 *         Date: 5/24/13
 */
public abstract class AbstractMessage<T> implements IMessage<T>{

    private Map<T, Integer> handledByListener = new HashMap<T, Integer>();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private List<T> handlersInOrder = new LinkedList<T>();


    @Override
    public void reset() {
        lock.writeLock().lock();
        try {
            handledByListener.clear();
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void handled(T listener) {
        lock.writeLock().lock();
        try {
	        handlersInOrder.add( listener );
            Integer count = handledByListener.get(listener);
            if(count == null){
                handledByListener.put(listener, 1);
            }
            else{
                handledByListener.put(listener, count + 1);
            }
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int getTimesHandled(T listener) {
        lock.readLock().lock();
        try {
            return handledByListener.containsKey(listener)
                ? handledByListener.get(listener)
                : 0;
        }finally {
            lock.readLock().unlock();
        }
    }

	public List<T> getHandledList() {
		lock.readLock().lock();
		try {
			return new ArrayList<T>( handlersInOrder );
		}finally {
			lock.readLock().unlock();
		}
	}
}
