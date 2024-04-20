package cs250.ec.counting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author John Maksuta
 * @since 2024-04-19
 * @see Course: CS250-801 Spring 2024,
 *      Instructor: Professor Pallickara,
 *      Assignment: Extra Credit Assignment
 */
public class AlphaHistogram {

    private KeyValueList list;

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                throw new Exception("The parameter is invalid. Expected length is 1.");
            }
            AlphaHistogram alphaHistogram = new AlphaHistogram();
            alphaHistogram.createList(args[0]);
            alphaHistogram.sortList();
            alphaHistogram.printList();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public AlphaHistogram() {
        super();
        this.list = new KeyValueList();
    }

    public void createList(String sentence) {
        if (!sentence.isEmpty()) {
            for (int n = 0; n < sentence.length(); n++) {
                char chr = sentence.toLowerCase().charAt(n);
                if (chr >= 97 && chr <= 122) {
                    list.add(new KeyValue(sentence.toLowerCase().charAt(n)));

                }
            }
        }
    }

    public void sortList() {
        boolean isSorted = false;
        while (!isSorted) {
            isSorted = true;
            for (int n = 0; n < this.list.size() - 1; n++) {
                int compareNext = this.list.get(n).compareTo(this.list.get(n + 1));
                if (!(compareNext > 0)) {
                    KeyValue next = this.list.get(n + 1);
                    KeyValue curr = this.list.get(n);
                    this.list.set(n, next);
                    this.list.set(n + 1, curr);
                    isSorted = false;
                }
            }
        }
    }

    public void printList() {
        for (KeyValue value : this.list) {
            System.out.println(value);
        }
    }

    private class KeyValueList implements List<KeyValue> {

        ArrayList<KeyValue> list = new ArrayList<>();

        @Override
        public boolean add(KeyValue e) {
            boolean result = false;
            if (e != null) {
                if (!contains(e)) {
                    result = this.list.add(e);
                } else {
                    KeyValue kv = get(indexOf(e));
                    kv.value++;
                }
            }
            return result;
        }

        @Override
        public void add(int index, KeyValue obj) {
            if (obj != null) {
                if (!contains(obj)) {
                    list.add(index, obj);
                } else {
                    int foundIndex = this.indexOf(obj);
                    KeyValue keyValue = list.remove(foundIndex);
                    keyValue.value += obj.value;
                    list.add(index, keyValue);
                }
            }
        }

        @Override
        public boolean addAll(Collection<? extends KeyValue> c) {
            boolean hasChanged = false;
            for (KeyValue keyValue : c) {
                boolean added = this.add(keyValue);
                if (!hasChanged && added) {
                    hasChanged = true;
                }
            }
            return hasChanged;
        }

        @Override
        public boolean addAll(int index, Collection<? extends KeyValue> collection) {
            boolean hasChanged = false;
            for (int n = collection.size() - 1; n >= 0; n--) {
                KeyValue value = collection.toArray(new KeyValue[] {})[n];
                this.add(index, value);
                hasChanged = true;
            }
            return hasChanged;
        }

        @Override
        public void clear() {
            this.list.clear();
        }

        @Override
        public boolean contains(Object o) {
            boolean result = false;
            if (o != null) {
                if (o.getClass().equals(KeyValue.class)) {
                    result = contains((KeyValue) o);
                } else if (o.getClass().equals(Character.class)) {
                    result = contains((Character) o);
                }
            }
            return result;
        }

        private boolean contains(KeyValue kv) {
            return contains(kv.key);
        }

        private boolean contains(Character chr) {
            boolean result = false;
            for (KeyValue keyValue : this.list) {
                if (keyValue.key == chr) {
                    result = true;
                    break;
                }
            }
            return result;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            boolean result = true;
            for (Object item : c) {
                if (!contains(item)) {
                    result = false;
                    break;
                }
            }
            return result;
        }

        @Override
        public KeyValue get(int index) {
            return this.list.get(index);
        }

        @Override
        public int indexOf(Object o) {
            int result = -1;
            if (o.getClass().equals(KeyValue.class)) {
                result = indexOf(((KeyValue) o).key);
            } else if (o.getClass().equals(Character.class)) {
                result = indexOf((Character) o);
            }
            return result;
        }

        private int indexOf(Character chr) {
            int result = -1;
            for (int n = 0; n < this.list.size(); n++) {
                if (this.list.get(n).key == chr) {
                    result = n;
                    break;
                }
            }
            return result;
        }

        @Override
        public boolean isEmpty() {
            return (this.list.size() == 0);
        }

        @Override
        public Iterator<KeyValue> iterator() {
            return this.list.iterator();
        }

        @Override
        public int lastIndexOf(Object o) {
            int result = -1;
            if (o.getClass().equals(KeyValue.class)) {
                result = lastIndexOf(((KeyValue) o).key);
            } else if (o.getClass().equals(Character.class)) {
                result = lastIndexOf((Character) o);
            }
            return result;
        }

        private int lastIndexOf(Character chr) {
            int result = -1;
            for (int n = this.list.size() - 1; n >= 0; n--) {
                if (this.list.get(n).key == chr) {
                    result = n;
                    break;
                }
            }
            return result;
        }

        @Override
        public ListIterator<KeyValue> listIterator() {
            return this.list.listIterator();
        }

        @Override
        public ListIterator<KeyValue> listIterator(int index) {
            return this.list.listIterator(index);
        }

        @Override
        public boolean remove(Object o) {
            return remove(indexOf(o)) != null;
        }

        @Override
        public KeyValue remove(int index) {
            KeyValue result = null;
            if (index >= 0 && index < this.list.size()) {
                result = this.list.remove(index);
            }
            return result;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return this.list.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return this.list.retainAll(c);
        }

        @Override
        public KeyValue set(int index, KeyValue keyValue) {
            return this.list.set(index, keyValue);
        }

        @Override
        public int size() {
            return this.list.size();
        }

        @Override
        public List<KeyValue> subList(int fromIndex, int toIndex) {
            return this.list.subList(fromIndex, toIndex);
        }

        @Override
        public Object[] toArray() {
            return this.list.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return this.list.toArray(a);
        }

    }

    private class KeyValue implements Comparable<KeyValue> {

        private char key;
        private int value;

        private KeyValue() {
            super();
            this.key = 0;
            this.value = 0;
        }

        public KeyValue(char key) {
            this();
            this.key = key;
            this.value = 1;
        }

        @Override
        public int compareTo(KeyValue o) {
            int result = 0;
            result = this.value - o.value;
            if (result == 0) {
                result = o.key - this.key;
            }
            return result;
        }

        @Override
        public String toString() {
            return String.format("%s: %d", this.key, this.value);
        }

    }

}
