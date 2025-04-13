/**
 * This package contains all classes related to user application logic.
 * <p>
 * All classes in this package are thread-safe and follow the Singleton pattern where applicable.
 * <p>
 * Author: Choo Kye Yong
 * Version: 1.0
 */
package program.boundary.menuTemplate;

/*
 * MenuAction.java
 * This interface defines a contract for menu actions in the application. It contains a single method execute() that must be implemented by any class that implements this interface.
 * This allows for a consistent way to define and execute actions associated with menu items.
 */
@FunctionalInterface
public interface MenuAction {
    /*
     * execute: This method is called to perform the action associated with the menu item.
     * It does not take any parameters and does not return any value.
     */
    public void execute();
}
