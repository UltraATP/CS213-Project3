package clinic.cs213project3.model.util;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The full implementation of a generic List data structure.
 * @param <E> A generic object.
 * @author Aarush Desai, Rohan Vuppunuthula
 */
public class List<E> implements Iterable<E> {

    /**
     * Class defines the Iterator used to iterate the list data structure.
     * @param <E> A generic object.
     */
    private class ListIterator<E> implements Iterator<E> {

        private int index = 0;

        /**
         * Iterator method checks if position in the list has a successor.
         * @return True if successor exists, else False.
         */
        public boolean hasNext() {
            return index < size;
        }

        /**
         * Iterator method returns the next object in the list.
         * @return The next list element, else throws exception if no successor exists.
         */
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements left.");
            }
            return (E) objects[index++];
        }

    }

    private static final int INIT_SIZE = 0;
    private static final int INIT_CAPACITY = 4;
    private static final int GROWTH_SIZE = 4;
    private static final int NOT_FOUND = -1;

    private E[] objects;
    private int size;

    /**
     * Constructor makes an empty list with a capacity of 4 cells.
     */
    public List() {
        objects = (E[]) new Object[INIT_CAPACITY];
        size = INIT_SIZE;
    }

    /**
     * Searches for an object in the list and returns its index.
     * @param e The object being searched for.
     * @return The index of an object element if it exists, else -1.
     */
    private int find(E e) {
        for (int i = 0; i < size; i++) {
            if (objects[i].equals(e)) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Increases the capacity of list by 4 cells.
     * The method is called when the list gets full and needs more space.
     */
    private void grow() {
        E[] newObjects = (E[]) new Object[objects.length + GROWTH_SIZE];
        for (int i = 0; i < objects.length; i++) {
            newObjects[i] = objects[i];
        }
        objects = newObjects;
    }

    /**
     * Checks if an object is an element of the list.
     * @param e The object we are searching for.
     * @return True if the object exists in the list, else False.
     */
    public boolean contains(E e) {
        return find(e) != NOT_FOUND;
    }

    /**
     * Appends an object to the list.
     * @param e The object we are adding.
     */
    public void add(E e) {
        if (!contains(e)) {
            if (objects.length == size) { // When list is full...
                grow();
            }
            objects[size] = e;
            size++;
        }
    }

    /**
     * Deletes an object from the list.
     * @param e The object we are deleting.
     */
    public void remove(E e) {
        if (contains(e)) {
            int index = find(e); // Index to delete at...
            int lastIndex = size - 1;
            objects[index] = objects[lastIndex]; // Replace with last element...
            objects[lastIndex] = null; // Delete last element...
            size--;
        }
    }

    /**
     * Checks if the list is empty.
     * @return True if list is empty, else False.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Getter method returns the size/length of the list.
     * @return The integer size of the list.
     */
    public int size() {
        return size;
    }

    /**
     * Accesses the element at an index of the list.
     * @param index The list index.
     * @return The object at the index, else null if index is out of bounds.
     */
    public E get(int index) {
        if (0 <= index && index < size) {
            return objects[index];
        }
        return null;
    }

    /**
     * Sets the object value of the list at a given index.
     * @param index The index to set.
     * @param e The object to assign index at.
     */
    public void set(int index, E e) {
        if (0 <= index && index < size) {
            objects[index] = e;
        }
    }

    /**
     * Searches for an object in the list and returns its index.
     * @param e The object being searched for.
     * @return The index of an object element if it exists, else -1.
     */
    public int indexOf(E e) {
        return find(e);
    }

    /**
     * Method instantiates a new List Iterator object.
     * @return The List Iterator.
     */
    public Iterator<E> iterator() {
        return new ListIterator<E>();
    }

}
