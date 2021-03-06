

import java.io.*;
import java.math.*;
import java.util.*;
class B {

    public static void main(String[] args) throws Exception {

        BufferedReader read = new BufferedReader(new InputStreamReader(
                System.in));

        while (true) {
            String s;
            if ((s = read.readLine()) == null)
                break;
            s = s.trim();
            if (s.length() == 1) {
                int i = Integer.parseInt(s);
                if (i == 0) {
                    System.out.println(0);
                    continue;
                } else if (i == 1) {
                    System.out.println(1);
                    continue;
                } else if (i == 2) {
                    System.out.println(2);
                    continue;
                }
            }
            /*BigNum b = BigNum.TWO;
            BigNum ac = BigNum.TWO;
            while(!ac.toString().equals(s)) {
                ac = ac.add(BigNum.ONE);
                b = b.add(BigNum.TWO);
                //System.out.println(ac.toString() + "::" + b.toString());
                //Thread.sleep(1000);
            }*/
            BigInteger b = new BigInteger(s);
            b = b.multiply(new BigInteger("2"));
            b = b.subtract(new BigInteger("2"));
            System.out.println(b.toString());
        }

    }
}

class BigNum {

    static final int MAX = 1000;

    static final long QUOTE = 100000;

    static final int QUOTE_SIZE = 5;

    static BigNum ONE = new BigNum(1);

    static BigNum TWO = new BigNum(2);

    static BigNum ZERO = new BigNum(0);

    int max = 0;

    long[] mag;

    BigNum(int i) {
        mag = new long[MAX];
        for (int z = 0; z != MAX; z++) {
            mag[z] = 0;
        }
        while (i != 0) {
            mag[max++] = i % QUOTE;
            i /= QUOTE;
        }
    }

    BigNum(String s) {
        this(0);
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

