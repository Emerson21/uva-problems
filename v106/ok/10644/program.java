

import java.io.*;
import java.util.*;

class Main {

    public static BigNum valores[] = new BigNum[600];

    public static void main(String args[]) throws Exception {

        int casos = Integer.parseInt(readLine().trim());
        int caso = 1;
        valores[0] = BigNum.ONE;
        valores[1] = BigNum.ZERO;
        valores[2] = BigNum.ONE;

        for (int i = 4; i != 502; i += 2) {
            // soma ((i-2) + 1)
            // (i-2)*2
            valores[i] = valores[i - 2].multiply(i - 2 + 1);
        }

        while (casos-- != 0) {

            BigNum bg = BigNum.ZERO;
            int n = Integer.parseInt(readLine().trim());
            if (n % 2 == 1) {
            } else {
                bg = valores[n];
            }

            System.out.println("Case " + caso++ + ": " + bg);

        }

    }

    static String[] split(String s) {
        StringTokenizer st = new StringTokenizer(s, " ");
        String[] tokens = new String[st.countTokens()];
        for (int i = 0; i != tokens.length; i++) {
            tokens[i] = st.nextToken();
        }
        return tokens;
    }

    // le uma linha inteira
    static String readLine() throws IOException {

        int maxLg = 20;
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

    static int readIntLine() throws IOException {
        return Integer.parseInt(readLine());
    }

    static int[] splitIntLine() throws IOException {
        String[] l = split(readLine());
        int v[] = new int[l.length];
        for (int i = 0; i != l.length; i++) {
            v[i] = Integer.parseInt(l[i]);
        }
        return v;
    }

}

class BigNum {

    static final int MAX = 1000;

    static final long QUOTE = 100000;

    static final int QUOTE_SIZE = 5;

    static BigNum ONE = new BigNum(1);

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

