package AppKickstarter.Server;
//======================================================================
// Observer
/**
 * 
 * @author user
 *
 */
public abstract class Observer {
    public abstract void update();
    protected Subject subject;

    //------------------------------------------------------------
    // Observer
    /**
     * 
     * @param subject
     */
    public Observer(Subject subject) {
        this.subject = subject;
    } // Observer
} // Observer
