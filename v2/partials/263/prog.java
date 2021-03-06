import java.io.IOException;

class BigNum {

    static final int MAX = 1000;

    static final long QUOTE = 100000;

    static final int QUOTE_SIZE = 5;

    static BigNum THREE = new BigNum(3);

    static BigNum TWO = new BigNum(2);

    static BigNum ONE = new BigNum(1);

    static BigNum ZERO = new BigNum(0);

    int max = 0;

    long[] mag;

    BigNum(int i) {
        init(0);
    }

    BigNum(String s) {
        read(s);
    }
    
    private void init(int i) {
        mag = new long[MAX];
        for (int z = 0; z != MAX; z++) {
            mag[z] = 0;
        }
        while (i != 0) {
            mag[max++] = i % QUOTE;
            i /= QUOTE;
        }
    }
    
    public void read(String s) {
        init(0);
        s = s.trim();
        int size = s.length();
        int p = 0;
        for (; s.charAt(p) == 0 && p != size; p++)
            ;
        if (p == size) {
            return;
        }
        int i = size;
        String st;
        while (true) {
            if (i - p <= QUOTE_SIZE) {
                mag[max++] = Integer.parseInt(s.substring(p, i));
                break;
            }
            st = s.substring(i - QUOTE_SIZE, i);
            mag[max++] = Integer.parseInt(st);
            i -= QUOTE_SIZE;
        }
        if (mag[max - 1] == 0) {
            max--;
        }
    }

    public BigNum multiply(int val) {
        BigNum a = new BigNum(this);
        long tempToAdd = 0;
        for (int i = 0; i != a.max; i++) {
            a.mag[i] *= val;
            a.mag[i] += tempToAdd;
            tempToAdd = 0;
            if (a.mag[i] >= QUOTE) {
                tempToAdd = (a.mag[i] / QUOTE);
                a.mag[i] = a.mag[i] % QUOTE;
                if (i + 1 == a.max) {
                    a.max++;
                }
            }
        }
        return a;
    }

    BigNum(BigNum b) {
        this.max = b.max;
        mag = new long[MAX];
        for (int z = 0; z != MAX; z++) {
            mag[z] = b.mag[z];
        }
    }

    // both are positive, result is also positive
    void sub(BigNum b) {
        long temp;
        for (int i = 0; i != this.max; i++) {
            temp = this.mag[i] - b.mag[i];
            if (temp < 0) {
                // vou ter que puxar do anterior
                this.mag[i + 1]--;
                temp += QUOTE;
            }
            this.mag[i] = temp;
        }
        while (this.max != 0 && this.mag[this.max - 1] == 0)
            this.max--;
    }

    BigNum add(BigNum b) {
        long temp;
        BigNum th = this;
        if (th.max > b.max) {
            th = b;
            b = this;
        }
        BigNum a = new BigNum(th);
        a.max = b.max;
        for (int i = 0; i != b.max; i++) {
            temp = b.mag[i] + a.mag[i];
            if (temp >= QUOTE) {
                a.mag[i + 1]++;
                a.mag[i] = temp - QUOTE;
                if (i + 1 == a.max) {
                    a.max++;
                }
            } else {
                a.mag[i] = temp;
            }
        }
        return a;
    }

    /* adiciona e muda esse aqui, TESTAR!!! */
    void addMut(BigNum b) {
        int mx = b.max > this.max ? this.max : b.max, i;
        for (i = 0; i != mx; i++) {
            mag[i] += b.mag[i];
            if (mag[i] >= QUOTE) {
                mag[i + 1]++;
                mag[i] -= QUOTE;
                if (i + 1 == max) {
                    mag[i + 1] = 1;
                    max++;
                }
            }
        }
        for (; i < b.max; i++) {
            mag[i] = b.mag[i];
        }
        max = i;
    }

    /* multiply mutable */
    public void multiplyMut(int val) {
        long tempToAdd = 0;
        for (int i = 0; i != max; i++) {
            mag[i] *= val;
            mag[i] += tempToAdd;
            tempToAdd = 0;
            if (mag[i] >= QUOTE) {
                tempToAdd = (mag[i] / QUOTE);
                mag[i] = mag[i] % QUOTE;
                if (i + 1 == max) {
                    mag[i + 1] = 0;
                    max++;
                }
            }
        }
    }

    public String toString() {
        if (max == 0) {
            return "0";
        }
        StringBuffer s = new StringBuffer();
        long z, toAdd;
        for (int i = max; i > 0; i--) {
            if (i != max) {
                if (mag[i - 1] == 0) {
                    for (z = 0; z != QUOTE_SIZE; z++) {
                        s.append("0");
                    }
                    continue;
                }
                z = mag[i - 1];
                toAdd = QUOTE_SIZE;
                while (z != 0) {
                    z /= 10;
                    toAdd--;
                }
                // se nao for o primeiro objeto
                while (toAdd-- != 0) {
                    s.append("0");
                }
            }
            s.append("" + mag[i - 1]);
        }
        return s.toString();
    }

}

class Main {

    public static void main(String[] args) throws IOException {
try{
        String linha, novo;
        BigNum a, b = new BigNum(0), c;
        int v[] = new int[10];
        int i, j, k, len, tam;
        HashMap map = new HashMap();
        int prelen;

        while (true) {

            linha = readLine().trim();
            if (linha.equals("0"))
                return;
            System.out.println("Original number was " + linha);

            tam = 0;
            prelen = 0;
            //map.clear();
            map = new HashMap();
            map.put(linha,linha);
            fora: while (true) {

                tam++;
                len = linha.length();
                for (i = 0; i != 10; i++)
                    v[i] = 0;
                for (i = 0; i != len; i++)
                    v[linha.charAt(i) - '0']++;
                linha = "";
                novo = "";
                for (i = 9; i >= 0; i--)
                    for (j = 0; j != v[i]; j++)
                        linha = linha + ((char) (i + '0'));
                for (i = 0; i != 10; i++)
                    for (j = 0; j != v[i]; j++)
                        novo = novo + ((char) (i + '0'));
                a = new BigNum(linha);
                b = new BigNum(novo);
                System.out.print(a + " - " + b + " = ");
                a.sub(b);
                linha = a.toString();
                System.out.println(linha);
                // verifica se ja contem
                prelen++;
                if(map.containsKey(linha)) {
                    break fora;
                } else {
                    map.put(linha,null);
                }
                if(prelen>1001) return;
            }
            System.out.println("Chain length " + tam + "\n");

        }}catch(Exception ex) {/*while(true);*/}

    } // le uma linha inteira

    static String readLine() throws IOException {

        int maxLg = 100;
        byte lin[] = new byte[maxLg];
        int lg = 0, car = -1;

        while (lg < maxLg) {
            car = System.in.read();
            if ((car < 0) || (car == '\n')) {
                break;
            }
            lin[lg++] += car;
        }

        if ((car < 0) && (lg == 0)) {
            return (null); // eof
        }

        return (new String(lin, 0, lg));

    }

}


class HashMap extends AbstractMap 
{
    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    transient Entry[] table;
    transient int size;
    int threshold;
    final float loadFactor;
    transient volatile int modCount;

    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                                               initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                                               loadFactor);

        // Find a power of 2 >= initialCapacity
        int capacity = 1;
        while (capacity < initialCapacity) 
            capacity <<= 1;
    
        this.loadFactor = loadFactor;
        threshold = (int)(capacity * loadFactor);
        table = new Entry[capacity];
        init();
    }
    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }
    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        threshold = (int)(DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        table = new Entry[DEFAULT_INITIAL_CAPACITY];
        init();
    }
    public HashMap(HashMap m) {
        this(Math.max((int) (m.size() / DEFAULT_LOAD_FACTOR) + 1,
                      DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR);
        putAllForCreate(m);
    }
    void init() {
    }
    static final Object NULL_KEY = new Object();
    static Object maskNull(Object key) {
        return (key == null ? NULL_KEY : key);
    }
    static Object unmaskNull(Object key) {
        return (key == NULL_KEY ? null : key);
    }

    static int hash(Object x) {
        int h = x.hashCode();

        h += ~(h << 9);
        h ^=  (h >>> 14);
        h +=  (h << 4);
        h ^=  (h >>> 10);
        return h;
    }
    static boolean eq(Object x, Object y) {
        return x == y || x.equals(y);
    }
    static int indexFor(int h, int length) {
        return h & (length-1);
    }
    public int size() {
        return size;
    }
  
    public boolean isEmpty() {
        return size == 0;
    }

    public Object get(Object key) {
        Object k = maskNull(key);
        int hash = hash(k);
        int i = indexFor(hash, table.length);
        Entry e = table[i]; 
        while (true) {
            if (e == null)
                return e;
            if (e.hash == hash && eq(k, e.key)) 
                return e.value;
            e = e.next;
        }
    }

    public boolean containsKey(Object key) {
        Object k = maskNull(key);
        int hash = hash(k);
        int i = indexFor(hash, table.length);
        Entry e = table[i]; 
        while (e != null) {
            if (e.hash == hash && eq(k, e.key)) 
                return true;
            e = e.next;
        }
        return false;
    }

    Entry getEntry(Object key) {
        Object k = maskNull(key);
        int hash = hash(k);
        int i = indexFor(hash, table.length);
        Entry e = table[i]; 
        while (e != null && !(e.hash == hash && eq(k, e.key)))
            e = e.next;
        return e;
    }

    public Object put(Object key, Object value) {
        Object k = maskNull(key);
        int hash = hash(k);
        int i = indexFor(hash, table.length);

        for (Entry e = table[i]; e != null; e = e.next) {
            if (e.hash == hash && eq(k, e.key)) {
                Object oldValue = e.value;
                e.value = value;
                e.recordAccess(this);
                return oldValue;
            }
        }

        modCount++;
        addEntry(hash, k, value, i);
        return null;
    }

    private void putForCreate(Object key, Object value) {
        Object k = maskNull(key);
        int hash = hash(k);
        int i = indexFor(hash, table.length);

        /**
         * Look for preexisting entry for key.  This will never happen for
         * clone or deserialize.  It will only happen for construction if the
         * input Map is a sorted map whose ordering is inconsistent w/ equals.
         */
        for (Entry e = table[i]; e != null; e = e.next) {
            if (e.hash == hash && eq(k, e.key)) {
                e.value = value;
                return;
            }
        }

        createEntry(hash, k, value, i);
    }

    void putAllForCreate(HashMap m) {
        for (Iterator i = m.entrySet().iterator(); i.hasNext(); ) {
            Entry e = (Entry) i.next();
            putForCreate(e.getKey(), e.getValue());
        }
    }

    void resize(int newCapacity) {
        Entry[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        Entry[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int)(newCapacity * loadFactor);
    }

    /** 
     * Transfer all entries from current table to newTable.
     */
    void transfer(Entry[] newTable) {
        Entry[] src = table;
        int newCapacity = newTable.length;
        for (int j = 0; j < src.length; j++) {
            Entry e = src[j];
            if (e != null) {
                src[j] = null;
                do {
                    Entry next = e.next;
                    int i = indexFor(e.hash, newCapacity);  
                    e.next = newTable[i];
                    newTable[i] = e;
                    e = next;
                } while (e != null);
            }
        }
    }
    public void putAll(HashMap m) {
        int numKeysToBeAdded = m.size();
        if (numKeysToBeAdded == 0)
            return;

        if (numKeysToBeAdded > threshold) {
            int targetCapacity = (int)(numKeysToBeAdded / loadFactor + 1);
            if (targetCapacity > MAXIMUM_CAPACITY)
                targetCapacity = MAXIMUM_CAPACITY;
            int newCapacity = table.length;
            while (newCapacity < targetCapacity)
                newCapacity <<= 1;
            if (newCapacity > table.length)
                resize(newCapacity);
        }

        for (Iterator i = m.entrySet().iterator(); i.hasNext(); ) {
            Entry e = (Entry) i.next();
            put(e.getKey(), e.getValue());
        }
    }
  
    public Object remove(Object key) {
        Entry e = removeEntryForKey(key);
        return (e == null ? e : e.value);
    }

    /**
     * Removes and returns the entry associated with the specified key
     * in the HashMap.  Returns null if the HashMap contains no mapping
     * for this key.
     */
    Entry removeEntryForKey(Object key) {
        Object k = maskNull(key);
        int hash = hash(k);
        int i = indexFor(hash, table.length);
        Entry prev = table[i];
        Entry e = prev;

        while (e != null) {
            Entry next = e.next;
            if (e.hash == hash && eq(k, e.key)) {
                modCount++;
                size--;
                if (prev == e) 
                    table[i] = next;
                else
                    prev.next = next;
                e.recordRemoval(this);
                return e;
            }
            prev = e;
            e = next;
        }
   
        return e;
    }

    /**
     * Special version of remove for EntrySet.
     */
    Entry removeMapping(Object o) {
        if (!(o instanceof Entry))
            return null;

        Entry entry = (Entry)o;
        Object k = maskNull(entry.getKey());
        int hash = hash(k);
        int i = indexFor(hash, table.length);
        Entry prev = table[i];
        Entry e = prev;

        while (e != null) {
            Entry next = e.next;
            if (e.hash == hash && e.equals(entry)) {
                modCount++;
                size--;
                if (prev == e) 
                    table[i] = next;
                else
                    prev.next = next;
                e.recordRemoval(this);
                return e;
            }
            prev = e;
            e = next;
        }
   
        return e;
    }

    /**
     * Removes all mappings from this map.
     */
    public void clear() {
        modCount++;
        Entry tab[] = table;
        for (int i = 0; i < tab.length; i++) 
            tab[i] = null;
        size = 0;
    }
    public boolean containsValue(Object value) {
	if (value == null) 
            return containsNullValue();

	Entry tab[] = table;
        for (int i = 0; i < tab.length ; i++)
            for (Entry e = tab[i] ; e != null ; e = e.next)
                if (value.equals(e.value))
                    return true;
	return false;
    }

    private boolean containsNullValue() {
	Entry tab[] = table;
        for (int i = 0; i < tab.length ; i++)
            for (Entry e = tab[i] ; e != null ; e = e.next)
                if (e.value == null)
                    return true;
	return false;
    }

    void addEntry(int hash, Object key, Object value, int bucketIndex) {
        table[bucketIndex] = new Entry(this,hash, key, value, table[bucketIndex]);
        if (size++ >= threshold) 
            resize(2 * table.length);
    }

    void createEntry(int hash, Object key, Object value, int bucketIndex) {
        table[bucketIndex] = new Entry(this,hash, key, value, table[bucketIndex]);
        size++;
    }
    Iterator newKeyIterator()   {
        return new KeyIterator(this);
    }
    Iterator newValueIterator()   {
        return new ValueIterator(this);
    }
    Iterator newEntryIterator()   {
        return new EntryIterator(this);
    }
    private transient Set entrySet = null;

    public Set keySet() {
        Set ks = keySet;
        return (ks != null ? ks : (keySet = new KeySet(this)));
    }


    public Collection values() {
        Collection vs = values;
        return (vs != null ? vs : (values = new Values(this)));
    }


    public Set entrySet() {
        Set es = entrySet;
        return (es != null ? es : (entrySet = new EntrySet(this)));
    }

    // These methods are used when serializing HashSets
    int   capacity()     { return table.length; }
    float loadFactor()   { return loadFactor;   }
}

interface Iterator {
    boolean hasNext();
    Object next();
    void remove();
}


interface Collection {
    int size();
    boolean isEmpty();
    boolean contains(Object o);
    Iterator iterator();
    boolean add(Object o);
    boolean remove(Object o);
    boolean containsAll(Collection c);
    boolean addAll(Collection c);
    boolean removeAll(Collection c);
    boolean retainAll(Collection c);
    void clear();
    boolean equals(Object o);
    int hashCode();
}

interface Set {
    int size();
    Iterator iterator();
    void clear();
}


abstract class AbstractSet extends AbstractCollection implements Set{
    protected AbstractSet() {
    }

    public boolean equals(Object o) {
	if (o == this)
	    return true;

	if (!(o instanceof Set))
	    return false;
	Collection c = (Collection) o;
	if (c.size() != size())
	    return false;
        try {
            return containsAll(c);
        } catch(ClassCastException unused)   {
            return false;
        } catch(NullPointerException unused) {
            return false;
        }
    }

    public int hashCode() {
	int h = 0;
	Iterator i = iterator();
	while (i.hasNext()) {
	    Object obj = i.next();
            if (obj != null)
                h += obj.hashCode();
        }
	return h;
    }

    public boolean removeAll(Collection c) {
        boolean modified = false;

        if (size() > c.size()) {
            for (Iterator i = c.iterator(); i.hasNext(); )
                modified |= remove(i.next());
        } else {
            for (Iterator i = iterator(); i.hasNext(); ) {
                if(c.contains(i.next())) {
                    i.remove();
                    modified = true;
                }
            }
        }
        return modified;
    }

}
abstract class AbstractCollection implements Collection {
    protected AbstractCollection() {
    }

    public abstract Iterator iterator();
    public abstract int size();
    public boolean isEmpty() {
	return size() == 0;
    }

    public boolean contains(Object o) {
	Iterator e = iterator();
	if (o==null) {
	    while (e.hasNext())
		if (e.next()==null)
		    return true;
	} else {
	    while (e.hasNext())
		if (o.equals(e.next()))
		    return true;
	}
	return false;
    }

    public boolean add(Object o) {
	throw new UnsupportedOperationException();
    }
    public boolean remove(Object o) {
	Iterator e = iterator();
	if (o==null) {
	    while (e.hasNext()) {
		if (e.next()==null) {
		    e.remove();
		    return true;
		}
	    }
	} else {
	    while (e.hasNext()) {
		if (o.equals(e.next())) {
		    e.remove();
		    return true;
		}
	    }
	}
	return false;
    }

    public boolean containsAll(Collection c) {
	Iterator e = c.iterator();
	while (e.hasNext())
	    if(!contains(e.next()))
		return false;

	return true;
    }
    public boolean addAll(Collection c) {
	boolean modified = false;
	Iterator e = c.iterator();
	while (e.hasNext()) {
	    if(add(e.next()))
		modified = true;
	}
	return modified;
    }
    public boolean removeAll(Collection c) {
	boolean modified = false;
	Iterator e = iterator();
	while (e.hasNext()) {
	    if(c.contains(e.next())) {
		e.remove();
		modified = true;
	    }
	}
	return modified;
    }

    public boolean retainAll(Collection c) {
	boolean modified = false;
	Iterator e = iterator();
	while (e.hasNext()) {
	    if(!c.contains(e.next())) {
		e.remove();
		modified = true;
	    }
	}
	return modified;
    }
    public void clear() {
	Iterator e = iterator();
	while (e.hasNext()) {
	    e.next();
	    e.remove();
	}
    }

    public String toString() {
	StringBuffer buf = new StringBuffer();
	buf.append("[");

        Iterator i = iterator();
        boolean hasNext = i.hasNext();
        while (hasNext) {
            Object o = i.next();
            buf.append(o == this ? "(this Collection)" : String.valueOf(o));
            hasNext = i.hasNext();
            if (hasNext)
                buf.append(", ");
        }

	buf.append("]");
	return buf.toString();
    }
}



abstract class AbstractMap {
    protected AbstractMap() {
    }
    public int size() {
	return entrySet().size();
    }
    public boolean isEmpty() {
	return size() == 0;
    }
    public boolean containsValue(Object value) {
	Iterator i = entrySet().iterator();
	if (value==null) {
	    while (i.hasNext()) {
		Entry e = (Entry) i.next();
		if (e.getValue()==null)
		    return true;
	    }
	} else {
	    while (i.hasNext()) {
		Entry e = (Entry) i.next();
		if (value.equals(e.getValue()))
		    return true;
	    }
	}
	return false;
    }

    public boolean containsKey(Object key) {
	Iterator i = entrySet().iterator();
	if (key==null) {
	    while (i.hasNext()) {
		Entry e = (Entry) i.next();
		if (e.getKey()==null)
		    return true;
	    }
	} else {
	    while (i.hasNext()) {
		Entry e = (Entry) i.next();
		if (key.equals(e.getKey()))
		    return true;
	    }
	}
	return false;
    }

    public Object get(Object key) {
	Iterator i = entrySet().iterator();
	if (key==null) {
	    while (i.hasNext()) {
		Entry e = (Entry) i.next();
		if (e.getKey()==null)
		    return e.getValue();
	    }
	} else {
	    while (i.hasNext()) {
		Entry e = (Entry) i.next();
		if (key.equals(e.getKey()))
		    return e.getValue();
	    }
	}
	return null;
    }

    public Object put(Object key, Object value) {
	throw new UnsupportedOperationException();
    }

    public Object remove(Object key) {
	Iterator i = entrySet().iterator();
	Entry correctEntry = null;
	if (key==null) {
	    while (correctEntry==null && i.hasNext()) {
		Entry e = (Entry) i.next();
		if (e.getKey()==null)
		    correctEntry = e;
	    }
	} else {
	    while (correctEntry==null && i.hasNext()) {
	        Entry e = (Entry) i.next();
		if (key.equals(e.getKey()))
		    correctEntry = e;
	    }
	}

	Object oldValue = null;
	if (correctEntry !=null) {
	    oldValue = correctEntry.getValue();
	    i.remove();
	}
	return oldValue;
    }

    public void putAll(AbstractMap t) {
	Iterator i = t.entrySet().iterator();
	while (i.hasNext()) {
	    Entry e = (Entry) i.next();
	    put(e.getKey(), e.getValue());
	}
    }
    public void clear() {
	entrySet().clear();
    }

    transient volatile Set        keySet = null;
    transient volatile Collection values = null;

    public abstract Set keySet();
	
    public abstract Collection values();
    public abstract Set entrySet();

    public boolean equals(Object o) {
	if (o == this)
	    return true;

	if (!(o instanceof HashMap))
	    return false;
	HashMap t = (HashMap) o;
	if (t.size() != size())
	    return false;

        try {
            Iterator i = entrySet().iterator();
            while (i.hasNext()) {
                Entry e = (Entry) i.next();
                Object key = e.getKey();
                Object value = e.getValue();
                if (value == null) {
                    if (!(t.get(key)==null && t.containsKey(key)))
                        return false;
                } else {
                    if (!value.equals(t.get(key)))
                        return false;
                }
            }
        } catch(ClassCastException unused)   {
            return false;
        } catch(NullPointerException unused) {
            return false;
        }

	return true;
    }

    public int hashCode() {
	int h = 0;
	Iterator i = entrySet().iterator();
	while (i.hasNext())
	    h += i.next().hashCode();
	return h;
    }

    public String toString() {
	StringBuffer buf = new StringBuffer();
	buf.append("{");

	Iterator i = entrySet().iterator();
        boolean hasNext = i.hasNext();
        while (hasNext) {
            Entry e = (Entry) (i.next());
            Object key = e.getKey();
            Object value = e.getValue();
            buf.append((key == this ?  "(this Map)" : key) + "=" + 
                       (value == this ? "(this Map)": value));

            hasNext = i.hasNext();
            if (hasNext)
                buf.append(", ");
        }

	buf.append("}");
	return buf.toString();
    }
}



 class Entry {
    final Object key;
    Object value;
    final int hash;
    Entry next;
    HashMap map;
    Entry(HashMap map,int h, Object k, Object v, Entry n) {
        this.map = map;
        value = v; 
        next = n;
        key = k;
        hash = h;
    }
    public Object getKey() {
        return map.unmaskNull(key);
    }
    public Object getValue() {
        return value;
    }

    public Object setValue(Object newValue) {
        Object oldValue = value;
        value = newValue;
        return oldValue;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Entry))
            return false;
        Entry e = (Entry)o;
        Object k1 = getKey();
        Object k2 = e.getKey();
        if (k1 == k2 || (k1 != null && k1.equals(k2))) {
            Object v1 = getValue();
            Object v2 = e.getValue();
            if (v1 == v2 || (v1 != null && v1.equals(v2))) 
                return true;
        }
        return false;
    }
    public int hashCode() {
        return (key==HashMap.NULL_KEY ? 0 : key.hashCode()) ^
               (value==null   ? 0 : value.hashCode());
    }
    public String toString() {
        return getKey() + "=" + getValue();
    }
    void recordAccess(HashMap m) {
    }
    void recordRemoval(HashMap m) {
    }
}

 
 abstract class HashIterator implements Iterator {
     Entry next;                  // next entry to return
     int expectedModCount;        // For fast-fail 
     int index;                   // current slot 
     Entry current;               // current entry
     HashMap map;

     HashIterator(HashMap map) {
         this.map = map;
         expectedModCount = map.modCount;
         Entry[] t = map.table;
         int i = t.length;
         Entry n = null;
         if (map.size != 0) { // advance to first entry
             while (i > 0 && (n = t[--i]) == null)
                 ;
         }
         next = n;
         index = i;
     }

     public boolean hasNext() {
         return next != null;
     }

     Entry nextEntry() { 
         /*if (modCount != expectedModCount)
             throw new ConcurrentModificationException();*/
         Entry e = next;
         /*if (e == null) 
             throw new NoSuchElementException();*/
             
         Entry n = e.next;
         Entry[] t = map.table;
         int i = index;
         while (n == null && i > 0)
             n = t[--i];
         index = i;
         next = n;
         return current = e;
     }

     public void remove() {
         if (current == null)
             throw new IllegalStateException();
         /*if (modCount != expectedModCount)
             throw new ConcurrentModificationException();*/
         Object k = current.key;
         current = null;
         map.removeEntryForKey(k);
         expectedModCount = map.modCount;
     }

 }

 class ValueIterator extends HashIterator {
     ValueIterator(HashMap map) { super(map); }
     public Object next() {
         return nextEntry().value;
     }
 }

 class KeyIterator extends HashIterator {
     KeyIterator(HashMap map) { super(map); }
     public Object next() {
         return nextEntry().getKey();
     }
 }

 class EntryIterator extends HashIterator {
     EntryIterator(HashMap map) { super(map); }
     public Object next() {
         return nextEntry();
     }
 }

 class KeySet extends AbstractSet {
     HashMap map;
     KeySet(HashMap map) { this.map = map; }
     public Iterator iterator() {
         return map.newKeyIterator();
     }
     public int size() {
         return map.size;
     }
     public boolean contains(Object o) {
         return map.containsKey(o);
     }
     public boolean remove(Object o) {
         return map.removeEntryForKey(o) != null;
     }
     public void clear() {
         map.clear();
     }
 }

 class Values extends AbstractCollection {
     HashMap map;
     Values(HashMap map) { this.map = map; }
     public Iterator iterator() {
         return map.newValueIterator();
     }
     public int size() {
         return map.size;
     }
     public boolean contains(Object o) {
         return map.containsValue(o);
     }
     public void clear() {
         map.clear();
     }
 }

 class EntrySet extends AbstractSet {
     HashMap map;
     EntrySet(HashMap map) { this.map = map; }
     public Iterator iterator() {
         return map.newEntryIterator();
     }
     public boolean contains(Object o) {
         if (!(o instanceof Entry))
             return false;
         Entry e = (Entry)o;
         Entry candidate = map.getEntry(e.getKey());
         return candidate != null && candidate.equals(e);
     }
     public boolean remove(Object o) {
         return map.removeMapping(o) != null;
     }
     public int size() {
         return map.size;
     }
     public void clear() {
         map.clear();
     }
 }
