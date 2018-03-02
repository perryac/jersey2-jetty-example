package edomingues.restserver.rest;

public class RequestTracking {
    private static RequestTracking sSingleton;
    private Long fID;

    private RequestTracking() {
        fID = new Long(0);
    }

    public static RequestTracking getRequestTracker() {
        if (null == sSingleton) {
            sSingleton = new RequestTracking();
        }
        return sSingleton;
    }

    public synchronized long nextID() {
        long id = fID.longValue() + 1;
        // check for wraparound
        if (id < 0) {
            id = 1;
        }
        fID = id;
        return fID.longValue();
    }
}
