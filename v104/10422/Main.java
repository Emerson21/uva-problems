

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @author Guilherme
 */
class Main10422 {

    public static void main(String[] args) throws NumberFormatException,
            IOException {
        

        try {
	        

        int casos = Integer.parseInt(readLine().trim());

        for (int i = 0; i != casos; i++) {

            if (run()) {
                System.out.println("yes");
            } else {
                System.out.println("no");
            }

        }
} catch (Exception e) {
	while(true) {
	    
	}
}

    }

    private static int maxLg = 85;

    private static final boolean run() throws IOException {

        String s = readLine().trim();

        int p = s.indexOf('#');

        //System.out.println(s + "::");

        // base 10
        if (p == -1) {
            if (verifica(s, '0', '9')) {
                return true;
            }
            return false;
        }

        // # logo de cara
        if (p == 0) {
            return false;
        }

        // tenta ler a base
        int base = Integer.parseInt(s.substring(0, p));

        // base invalida
        if (base < 2 || base > 16) {
            return false;
        }

        s = s.substring(p);
        //System.out.println(s + "," + base);

        int atual = 1, inicial = 1, len = s.length();
        for (; atual < len;) {

            char cv = s.charAt(atual);

            // fim
            if (cv == '#') {

                if (atual == inicial) {
                    return false;
                }

                int antigo = inicial;

                inicial = atual + 1;
                if (inicial == len) {
                    // acabou, correto
                    return true;
                }
                
                // se nao acabou
                if (s.charAt(inicial) != '#') {
                    // invalido
                    return false;
                }

                // traduz o numero para a base escolhida
                if(antigo!=1){ antigo++;}
                //BCG l = new BCG(s.substring(antigo, atual), base);
                // nova base
                //base = l.intValue();
                //if (base > 16 || base < 2) {
                //    return false;
                //}

                atual++;

            } else if (base < 11) {
                // verificacao do digito atual
                if (cv < '0' || cv >= ('0' + base)) {
                    //System.out.println("digito menor que a base: " + cv);
                    return false;
                }
            } else {
                // add verificacao do digito atual
                if (!((cv >= '0' && cv < ('0' + base)) || (cv >= 'a' && cv < ('a' + base - 10)))) {
                    //System.out.println("digito menor que a base: " + cv);
                    return false;
                }
            }

            atual++;

        }

        return false;

    }

    /**
     * @param s
     * @param c
     * @param d
     * @return
     */
    private static boolean verifica(String s, char c, char d) {
        for (int i = 0; i != s.length(); i++) {
            if ((s.charAt(i) > d || s.charAt(i) < c)) {
                return false;
            }
        }
        return true;
    }

    static String[] split(String s) {
        StringTokenizer st = new StringTokenizer(s, "#");
        String[] tokens = new String[st.countTokens()];
        for (int i = 0; i != tokens.length; i++) {
            tokens[i] = st.nextToken();
        }
        return tokens;
    }

    // le uma linha inteira
    static String readLine() throws IOException {
        byte lin[] = new byte[maxLg = 500];
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
class BCG {
    int signum;
    int[] mag;
    private int firstNonzeroIntNum = -2;
    private final static long LONG_MASK = 0xffffffffL;
    public int intValue() {
    	int result = 0;
    	result = getInt(0);
    	return result;
        }
    public BCG(byte[] val) {
	if (val.length == 0)
	    throw new NumberFormatException("Zero length BCG");

	if (val[0] < 0) {
            mag = makePositive(val);
	    signum = -1;
	} else {
	    mag = stripLeadingZeroBytes(val);
	    signum = (mag.length == 0 ? 0 : 1);
	}
    }

    BCG(String val, int radix) {
	int cursor = 0, numDigits;
        int len = val.length();

	if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
	    throw new NumberFormatException("Radix out of range");
	if (val.length() == 0)
	    throw new NumberFormatException("Zero length BCG");

	// Check for minus sign
	signum = 1;
        int index = val.indexOf('-');
        if (index != -1) {
            if (index == 0) {
                if (val.length() == 1)
                    throw new NumberFormatException("Zero length BCG");
                signum = -1;
                cursor = 1;
            } else {
                throw new NumberFormatException("Illegal embedded minus sign");
            }
        }

        // Skip leading zeros and compute number of digits in magnitude
	while (cursor < len &&
               Character.digit(val.charAt(cursor),radix) == 0)
	    cursor++;
	if (cursor == len) {
	    signum = 0;
	    mag = ZERO.mag;
	    return;
	} else {
	    numDigits = len - cursor;
	}

        // Pre-allocate array of expected size. May be too large but can
        // never be too small. Typically exact.
        int numBits = (int)(((numDigits * bitsPerDigit[radix]) >>> 10) + 1);
        int numWords = (numBits + 31) /32;
        mag = new int[numWords];

	// Process first (potentially short) digit group
	int firstGroupLen = numDigits % digitsPerInt[radix];
	if (firstGroupLen == 0)
	    firstGroupLen = digitsPerInt[radix];
	String group = val.substring(cursor, cursor += firstGroupLen);
        mag[mag.length - 1] = Integer.parseInt(group, radix);
	if (mag[mag.length - 1] < 0)
	    throw new NumberFormatException("Illegal digit");
        
	// Process remaining digit groups
        int superRadix = intRadix[radix];
        int groupVal = 0;
	while (cursor < val.length()) {
	    group = val.substring(cursor, cursor += digitsPerInt[radix]);
	    groupVal = Integer.parseInt(group, radix);
	    if (groupVal < 0)
		throw new NumberFormatException("Illegal digit");
            destructiveMulAdd(mag, superRadix, groupVal);
	}
        // Required for cases where the array was overallocated.
        mag = trustedStripLeadingZeroInts(mag);
    }

    private static long bitsPerDigit[] = { 0, 0,
        1024, 1624, 2048, 2378, 2648, 2875, 3072, 3247, 3402, 3543, 3672,
        3790, 3899, 4001, 4096, 4186, 4271, 4350, 4426, 4498, 4567, 4633,
        4696, 4756, 4814, 4870, 4923, 4975, 5025, 5074, 5120, 5166, 5210,
                                           5253, 5295};

    private static void destructiveMulAdd(int[] x, int y, int z) {
        // Perform the multiplication word by word
        long ylong = y & LONG_MASK;
        long zlong = z & LONG_MASK;
        int len = x.length;

        long product = 0;
        long carry = 0;
        for (int i = len-1; i >= 0; i--) {
            product = ylong * (x[i] & LONG_MASK) + carry;
            x[i] = (int)product;
            carry = product >>> 32;
        }

        // Perform the addition
        long sum = (x[len-1] & LONG_MASK) + zlong;
        x[len-1] = (int)sum;
        carry = sum >>> 32;
        for (int i = len-2; i >= 0; i--) {
            sum = (x[i] & LONG_MASK) + carry;
            x[i] = (int)sum;
            carry = sum >>> 32;
        }
    }

    public BCG(String val) {
	this(val, 10);
    }

    private BCG(int[] magnitude, int signum) {
	this.signum = (magnitude.length==0 ? 0 : signum);
	this.mag = magnitude;
    }

    BCG(MutableBCG val, int sign) {
        if (val.offset > 0 || val.value.length != val.intLen) {
            mag = new int[val.intLen];
            for(int i=0; i<val.intLen; i++)
                mag[i] = val.value[val.offset+i];
        } else {
            mag = val.value;
        }

	this.signum = (val.intLen == 0) ? 0 : sign;
    }

    private static BCG valueOf(long val) {
	// If -MAX_CONSTANT < val < MAX_CONSTANT, return stashed constant
	if (val == 0)
	    return ZERO;
	if (val > 0 && val <= MAX_CONSTANT)
	    return posConst[(int) val];
	else if (val < 0 && val >= -MAX_CONSTANT)
	    return negConst[(int) -val];

	return new BCG(val);
    }

    private BCG(long val) {
        if (val < 0) {
            signum = -1;
            val = -val;
        } else {
            signum = 1;
        }

        int highWord = (int)(val >>> 32);
        if (highWord==0) {
            mag = new int[1];
            mag[0] = (int)val;
        } else {
            mag = new int[2];
            mag[0] = highWord;
            mag[1] = (int)val;
        }
    }

    private final static int MAX_CONSTANT = 16;
    private static BCG posConst[] = new BCG[MAX_CONSTANT+1];
    private static BCG negConst[] = new BCG[MAX_CONSTANT+1];
    static {
	for (int i = 1; i <= MAX_CONSTANT; i++) {
	    int[] magnitude = new int[1];
	    magnitude[0] = (int) i;
	    posConst[i] = new BCG(magnitude,  1);
	    negConst[i] = new BCG(magnitude, -1);
	}
    }

    public static final BCG ZERO = new BCG(new int[0], 0);

    public static final BCG ONE = valueOf(1);

    // shifts a up to len left n bits assumes no leading zeros, 0<=n<32
    static void primitiveLeftShift(int[] a, int len, int n) {
        if (len == 0 || n == 0)
            return;

        int n2 = 32 - n;
        for (int i=0, c=a[i], m=i+len-1; i<m; i++) {
            int b = c;
            c = a[i+1];
            a[i] = (b << n) | (c >>> n2);
        }
        a[len-1] <<= n;
    }

    private BCG abs() {
	return (signum >= 0 ? this : this.negate());
    }

    private BCG negate() {
	return new BCG(this.mag, -this.signum);
    }

    private BCG shiftLeft(int n) {
        if (signum == 0)
            return ZERO;
        if (n==0)
            return this;
        if (n<0)
            return shiftRight(-n);

        int nInts = n >>> 5;
        int nBits = n & 0x1f;
        int magLen = mag.length;
        int newMag[] = null;

        if (nBits == 0) {
            newMag = new int[magLen + nInts];
            for (int i=0; i<magLen; i++)
                newMag[i] = mag[i];
        } else {
            int i = 0;
            int nBits2 = 32 - nBits;
            int highBits = mag[0] >>> nBits2;
            if (highBits != 0) {
                newMag = new int[magLen + nInts + 1];
                newMag[i++] = highBits;
            } else {
                newMag = new int[magLen + nInts];
            }
            int j=0;
            while (j < magLen-1)
                newMag[i++] = mag[j++] << nBits | mag[j] >>> nBits2;
            newMag[i] = mag[j] << nBits;
        }

        return new BCG(newMag, signum);
    }

    private BCG shiftRight(int n) {
        if (n==0)
            return this;
        if (n<0)
            return shiftLeft(-n);

        int nInts = n >>> 5;
        int nBits = n & 0x1f;
        int magLen = mag.length;
        int newMag[] = null;

        // Special case: entire contents shifted off the end
        if (nInts >= magLen)
            return (signum >= 0 ? ZERO : negConst[1]);

        if (nBits == 0) {
            int newMagLen = magLen - nInts;
            newMag = new int[newMagLen];
            for (int i=0; i<newMagLen; i++)
                newMag[i] = mag[i];
        } else {
            int i = 0;
            int highBits = mag[0] >>> nBits;
            if (highBits != 0) {
                newMag = new int[magLen - nInts];
                newMag[i++] = highBits;
            } else {
                newMag = new int[magLen - nInts -1];
            }

            int nBits2 = 32 - nBits;
            int j=0;
            while (j < magLen - nInts - 1)
                newMag[i++] = (mag[j++] << nBits2) | (mag[j] >>> nBits);
        }

        if (signum < 0) {
            // Find out whether any one-bits were shifted off the end.
            boolean onesLost = false;
            for (int i=magLen-1, j=magLen-nInts; i>=j && !onesLost; i--)
                onesLost = (mag[i] != 0);
            if (!onesLost && nBits != 0)
                onesLost = (mag[magLen - nInts - 1] << (32 - nBits) != 0);

            if (onesLost)
                newMag = javaIncrement(newMag);
        }

        return new BCG(newMag, signum);
    }

    private int[] javaIncrement(int[] val) {
        int lastSum = 0;
        for (int i=val.length-1;  i >= 0 && lastSum == 0; i--)
            lastSum = (val[i] += 1);
        if (lastSum == 0) {
            val = new int[val.length+1];
            val[0] = 1;
        }
        return val;
    }

    static int bitLen(int w) {
        // Binary search - decision tree (5 tests, rarely 6)
        return
         (w < 1<<15 ?
          (w < 1<<7 ?
           (w < 1<<3 ?
            (w < 1<<1 ? (w < 1<<0 ? (w<0 ? 32 : 0) : 1) : (w < 1<<2 ? 2 : 3)) :
            (w < 1<<5 ? (w < 1<<4 ? 4 : 5) : (w < 1<<6 ? 6 : 7))) :
           (w < 1<<11 ?
            (w < 1<<9 ? (w < 1<<8 ? 8 : 9) : (w < 1<<10 ? 10 : 11)) :
            (w < 1<<13 ? (w < 1<<12 ? 12 : 13) : (w < 1<<14 ? 14 : 15)))) :
          (w < 1<<23 ?
           (w < 1<<19 ?
            (w < 1<<17 ? (w < 1<<16 ? 16 : 17) : (w < 1<<18 ? 18 : 19)) :
            (w < 1<<21 ? (w < 1<<20 ? 20 : 21) : (w < 1<<22 ? 22 : 23))) :
           (w < 1<<27 ?
            (w < 1<<25 ? (w < 1<<24 ? 24 : 25) : (w < 1<<26 ? 26 : 27)) :
            (w < 1<<29 ? (w < 1<<28 ? 28 : 29) : (w < 1<<30 ? 30 : 31)))));
    }

    final static byte trailingZeroTable[] = {
      -25, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	6, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	7, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	6, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	5, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0,
	4, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 2, 0, 1, 0};

    static int trailingZeroCnt(int val) {
        // Loop unrolled for performance
        int byteVal = val & 0xff;
        if (byteVal != 0)
            return trailingZeroTable[byteVal];

        byteVal = (val >>> 8) & 0xff;
        if (byteVal != 0)
            return trailingZeroTable[byteVal] + 8;

        byteVal = (val >>> 16) & 0xff;
        if (byteVal != 0)
            return trailingZeroTable[byteVal] + 16;

        byteVal = (val >>> 24) & 0xff;
        return trailingZeroTable[byteVal] + 24;
    }
    public boolean equals(Object x) {
	// This test is just an optimization, which may or may not help
	if (x == this)
	    return true;

	if (!(x instanceof BCG))
	    return false;
	BCG xInt = (BCG) x;

	if (xInt.signum != signum || xInt.mag.length != mag.length)
	    return false;

	for (int i=0; i<mag.length; i++)
	    if (xInt.mag[i] != mag[i])
		return false;

	return true;
    }

    public int hashCode() {
	int hashCode = 0;

	for (int i=0; i<mag.length; i++)
	    hashCode = (int)(31*hashCode + (mag[i] & LONG_MASK));

	return hashCode * signum;
    }

    private String toString(int radix) {
	if (signum == 0)
	    return "0";
	if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
	    radix = 10;

	// Compute upper bound on number of digit groups and allocate space
	int maxNumDigitGroups = (4*mag.length + 6)/7;
	String digitGroup[] = new String[maxNumDigitGroups];
        
	// Translate number to string, a digit group at a time
	BCG tmp = this.abs();
	int numGroups = 0;
	while (tmp.signum != 0) {
            BCG d = longRadix[radix];

            MutableBCG q = new MutableBCG(),
                              r = new MutableBCG(),
                              a = new MutableBCG(tmp.mag),
                              b = new MutableBCG(d.mag);
            a.divide(b, q, r);
            BCG q2 = new BCG(q, tmp.signum * d.signum);
            BCG r2 = new BCG(r, tmp.signum * d.signum);

            digitGroup[numGroups++] = Long.toString(r2.longValue(), radix);
            tmp = q2;
	}

	// Put sign (if any) and first digit group into result buffer
	StringBuffer buf = new StringBuffer(numGroups*digitsPerLong[radix]+1);
	if (signum<0)
	    buf.append('-');
	buf.append(digitGroup[numGroups-1]);

	// Append remaining digit groups padded with leading zeros
	for (int i=numGroups-2; i>=0; i--) {
	    // Prepend (any) leading zeros for this digit group
	    int numLeadingZeros = digitsPerLong[radix]-digitGroup[i].length();
	    if (numLeadingZeros != 0)
		buf.append(zeros[numLeadingZeros]);
	    buf.append(digitGroup[i]);
	}
	return buf.toString();
    }

    /* zero[i] is a string of i consecutive zeros. */
    private static String zeros[] = new String[64];
    static {
	zeros[63] =
	    "000000000000000000000000000000000000000000000000000000000000000";
	for (int i=0; i<63; i++)
	    zeros[i] = zeros[63].substring(0, i);
    }

    public String toString() {
	return toString(10);
    }
    private long longValue() {
	long result = 0;

	for (int i=1; i>=0; i--)
	    result = (result << 32) + (getInt(i) & LONG_MASK);
	return result;
    }

    private static int[] trustedStripLeadingZeroInts(int val[]) {
        int keep;

	// Find first nonzero byte
        for (keep=0; keep<val.length && val[keep]==0; keep++)
            ;

        // Only perform copy if necessary
        if (keep > 0) {
            int result[] = new int[val.length - keep];
            for(int i=0; i<val.length - keep; i++)
               result[i] = val[keep+i];
            return result; 
        }
        return val;
    }

    private static int[] stripLeadingZeroBytes(byte a[]) {
        int byteLength = a.length;
	int keep;

	// Find first nonzero byte
	for (keep=0; keep<a.length && a[keep]==0; keep++)
	    ;

	// Allocate new array and copy relevant part of input array
        int intLength = ((byteLength - keep) + 3)/4;
	int[] result = new int[intLength];
        int b = byteLength - 1;
        for (int i = intLength-1; i >= 0; i--) {
            result[i] = a[b--] & 0xff;
            int bytesRemaining = b - keep + 1;
            int bytesToTransfer = Math.min(3, bytesRemaining);
            for (int j=8; j <= 8*bytesToTransfer; j += 8)
                result[i] |= ((a[b--] & 0xff) << j);
        }
        return result;
    }

    private static int[] makePositive(byte a[]) {
	int keep, k;
        int byteLength = a.length;

	for (keep=0; keep<byteLength && a[keep]==-1; keep++)
	    ;

	for (k=keep; k<byteLength && a[k]==0; k++)
	    ;

	int extraByte = (k==byteLength) ? 1 : 0;
        int intLength = ((byteLength - keep + extraByte) + 3)/4;
	int result[] = new int[intLength];

        int b = byteLength - 1;
        for (int i = intLength-1; i >= 0; i--) {
            result[i] = a[b--] & 0xff;
            int numBytesToTransfer = Math.min(3, b-keep+1);
            if (numBytesToTransfer < 0)
                numBytesToTransfer = 0;
            for (int j=8; j <= 8*numBytesToTransfer; j += 8)
                result[i] |= ((a[b--] & 0xff) << j);

            // Mask indicates which bits must be complemented
            int mask = -1 >>> (8*(3-numBytesToTransfer));
            result[i] = ~result[i] & mask;
        }

	// Add one to one's complement to generate two's complement
	for (int i=result.length-1; i>=0; i--) {
            result[i] = (int)((result[i] & LONG_MASK) + 1);
	    if (result[i] != 0)
                break;
        }

	return result;
    }

    private static int digitsPerLong[] = {0, 0,
	62, 39, 31, 27, 24, 22, 20, 19, 18, 18, 17, 17, 16, 16, 15, 15, 15, 14,
	14, 14, 14, 13, 13, 13, 13, 13, 13, 12, 12, 12, 12, 12, 12, 12, 12};

    private static BCG longRadix[] = {null, null,
        valueOf(0x4000000000000000L), valueOf(0x383d9170b85ff80bL),
	valueOf(0x4000000000000000L), valueOf(0x6765c793fa10079dL),
	valueOf(0x41c21cb8e1000000L), valueOf(0x3642798750226111L),
        valueOf(0x1000000000000000L), valueOf(0x12bf307ae81ffd59L),
	valueOf( 0xde0b6b3a7640000L), valueOf(0x4d28cb56c33fa539L),
	valueOf(0x1eca170c00000000L), valueOf(0x780c7372621bd74dL),
	valueOf(0x1e39a5057d810000L), valueOf(0x5b27ac993df97701L),
	valueOf(0x1000000000000000L), valueOf(0x27b95e997e21d9f1L),
	valueOf(0x5da0e1e53c5c8000L), valueOf( 0xb16a458ef403f19L),
	valueOf(0x16bcc41e90000000L), valueOf(0x2d04b7fdd9c0ef49L),
	valueOf(0x5658597bcaa24000L), valueOf( 0x6feb266931a75b7L),
	valueOf( 0xc29e98000000000L), valueOf(0x14adf4b7320334b9L),
	valueOf(0x226ed36478bfa000L), valueOf(0x383d9170b85ff80bL),
	valueOf(0x5a3c23e39c000000L), valueOf( 0x4e900abb53e6b71L),
	valueOf( 0x7600ec618141000L), valueOf( 0xaee5720ee830681L),
	valueOf(0x1000000000000000L), valueOf(0x172588ad4f5f0981L),
	valueOf(0x211e44f7d02c1000L), valueOf(0x2ee56725f06e5c71L),
	valueOf(0x41c21cb8e1000000L)};

    private static int digitsPerInt[] = {0, 0, 30, 19, 15, 13, 11,
        11, 10, 9, 9, 8, 8, 8, 8, 7, 7, 7, 7, 7, 7, 7, 6, 6, 6, 6,
        6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 5};

    private static int intRadix[] = {0, 0,
        0x40000000, 0x4546b3db, 0x40000000, 0x48c27395, 0x159fd800,
        0x75db9c97, 0x40000000, 0x17179149, 0x3b9aca00, 0xcc6db61,
        0x19a10000, 0x309f1021, 0x57f6c100, 0xa2f1b6f,  0x10000000,
        0x18754571, 0x247dbc80, 0x3547667b, 0x4c4b4000, 0x6b5a6e1d,
        0x6c20a40,  0x8d2d931,  0xb640000,  0xe8d4a51,  0x1269ae40,
        0x17179149, 0x1cb91000, 0x23744899, 0x2b73a840, 0x34e63b41,
        0x40000000, 0x4cfa3cc1, 0x5c13d840, 0x6d91b519, 0x39aa400
    };

    private int signInt() {
	return (int) (signum < 0 ? -1 : 0);
    }

    private int getInt(int n) {
        if (n < 0)
            return 0;
	if (n >= mag.length)
	    return signInt();

	int magInt = mag[mag.length-n-1];

	return (int) (signum >= 0 ? magInt :
		       (n <= firstNonzeroIntNum() ? -magInt : ~magInt));
    }

     private int firstNonzeroIntNum() {
	if (firstNonzeroIntNum == -2) {
	    // Search for the first nonzero int
	    int i;
	    for (i=mag.length-1; i>=0 && mag[i]==0; i--)
		;
	    firstNonzeroIntNum = mag.length-i-1;
	}
	return firstNonzeroIntNum;
    }

}
class MutableBCG {
	int[] value;
	int intLen;
	int offset = 0;
	private final static long LONG_MASK = 0xffffffffL;
	MutableBCG() {
		value = new int[1];
		intLen = 0;
	}
	MutableBCG(int[] val) {
		value = val;
		intLen = val.length;
	}
	private void clear() {
		offset = intLen = 0;
		for (int index = 0, n = value.length; index < n; index++)
			value[index] = 0;
	}
	private final int compare(MutableBCG b) {
		if (intLen < b.intLen) return -1;
		if (intLen > b.intLen) return 1;
		for (int i = 0; i < intLen; i++) {
			int b1 = value[offset + i] + 0x80000000;
			int b2 = b.value[b.offset + i] + 0x80000000;
			if (b1 < b2) return -1;
			if (b1 > b2) return 1;
		}
		return 0;
	}
	private final void normalize() {
		if (intLen == 0) {
			offset = 0;
			return;
		}
		int index = offset;
		if (value[index] != 0) return;
		int indexBound = index + intLen;
		do {
			index++;
		} while (index < indexBound && value[index] == 0);
		int numZeros = index - offset;
		intLen -= numZeros;
		offset = (intLen == 0 ? 0 : offset + numZeros);
	}
	private void setValue(int[] val, int length) {
		value = val;
		intLen = length;
		offset = 0;
	}
	private void copyValue(MutableBCG val) {
		int len = val.intLen;
		if (value.length < len) value = new int[len];
		for (int i = 0; i < len; i++)
			value[i] = val.value[val.offset + i];
		intLen = len;
		offset = 0;
	}
	public String toString() {
		BCG b = new BCG(this, 1);
		return b.toString();
	}
	private void rightShift(int n) {
		if (intLen == 0) return;
		int nInts = n >>> 5;
		int nBits = n & 0x1F;
		this.intLen -= nInts;
		if (nBits == 0) return;
		int bitsInHighWord = BCG.bitLen(value[offset]);
		if (nBits >= bitsInHighWord) {
			this.primitiveLeftShift(32 - nBits);
			this.intLen--;
		} else {
			primitiveRightShift(nBits);
		}
	}
	private void leftShift(int n) {
		if (intLen == 0) return;
		int nInts = n >>> 5;
		int nBits = n & 0x1F;
		int bitsInHighWord = BCG.bitLen(value[offset]);
		// If shift can be done without moving words, do so
		if (n <= (32 - bitsInHighWord)) {
			primitiveLeftShift(nBits);
			return;
		}
		int newLen = intLen + nInts + 1;
		if (nBits <= (32 - bitsInHighWord)) newLen--;
		if (value.length < newLen) {
			// The array must grow
			int[] result = new int[newLen];
			for (int i = 0; i < intLen; i++)
				result[i] = value[offset + i];
			setValue(result, newLen);
		} else if (value.length - offset >= newLen) {
			// Use space on right
			for (int i = 0; i < newLen - intLen; i++)
				value[offset + intLen + i] = 0;
		} else {
			// Must use space on left
			for (int i = 0; i < intLen; i++)
				value[i] = value[offset + i];
			for (int i = intLen; i < newLen; i++)
				value[i] = 0;
			offset = 0;
		}
		intLen = newLen;
		if (nBits == 0) return;
		if (nBits <= (32 - bitsInHighWord)) primitiveLeftShift(nBits);
		else primitiveRightShift(32 - nBits);
	}
	private int divadd(int[] a, int[] result, int offset) {
		long carry = 0;
		for (int j = a.length - 1; j >= 0; j--) {
			long sum = (a[j] & LONG_MASK) + (result[j + offset] & LONG_MASK)
					+ carry;
			result[j + offset] = (int) sum;
			carry = sum >>> 32;
		}
		return (int) carry;
	}
	private int mulsub(int[] q, int[] a, int x, int len, int offset) {
		long xLong = x & LONG_MASK;
		long carry = 0;
		offset += len;
		for (int j = len - 1; j >= 0; j--) {
			long product = (a[j] & LONG_MASK) * xLong + carry;
			long difference = q[offset] - product;
			q[offset--] = (int) difference;
			carry = (product >>> 32)
					+ (((difference & LONG_MASK) > (((~(int) product) & LONG_MASK))) ? 1
							: 0);
		}
		return (int) carry;
	}
	private final void primitiveRightShift(int n) {
		int[] val = value;
		int n2 = 32 - n;
		for (int i = offset + intLen - 1, c = val[i]; i > offset; i--) {
			int b = c;
			c = val[i - 1];
			val[i] = (c << n2) | (b >>> n);
		}
		val[offset] >>>= n;
	}
	private final void primitiveLeftShift(int n) {
		int[] val = value;
		int n2 = 32 - n;
		for (int i = offset, c = val[i], m = i + intLen - 1; i < m; i++) {
			int b = c;
			c = val[i + 1];
			val[i] = (b << n) | (c >>> n2);
		}
		val[offset + intLen - 1] <<= n;
	}
	void mul(int y, MutableBCG z) {
		if (y == 1) {
			z.copyValue(this);
			return;
		}
		if (y == 0) {
			z.clear();
			return;
		}
		// Perform the multiplication word by word
		long ylong = y & LONG_MASK;
		int[] zval = (z.value.length < intLen + 1 ? new int[intLen + 1]
				: z.value);
		long carry = 0;
		for (int i = intLen - 1; i >= 0; i--) {
			long product = ylong * (value[i + offset] & LONG_MASK) + carry;
			zval[i + 1] = (int) product;
			carry = product >>> 32;
		}
		if (carry == 0) {
			z.offset = 1;
			z.intLen = intLen;
		} else {
			z.offset = 0;
			z.intLen = intLen + 1;
			zval[0] = (int) carry;
		}
		z.value = zval;
	}
	void divideOneWord(int divisor, MutableBCG quotient) {
		long divLong = divisor & LONG_MASK;
		// Special case of one word dividend
		if (intLen == 1) {
			long remValue = value[offset] & LONG_MASK;
			quotient.value[0] = (int) (remValue / divLong);
			quotient.intLen = (quotient.value[0] == 0) ? 0 : 1;
			quotient.offset = 0;
			value[0] = (int) (remValue - (quotient.value[0] * divLong));
			offset = 0;
			intLen = (value[0] == 0) ? 0 : 1;
			return;
		}
		if (quotient.value.length < intLen) quotient.value = new int[intLen];
		quotient.offset = 0;
		quotient.intLen = intLen;
		// Normalize the divisor
		int shift = 32 - BCG.bitLen(divisor);
		int rem = value[offset];
		long remLong = rem & LONG_MASK;
		if (remLong < divLong) {
			quotient.value[0] = 0;
		} else {
			quotient.value[0] = (int) (remLong / divLong);
			rem = (int) (remLong - (quotient.value[0] * divLong));
			remLong = rem & LONG_MASK;
		}
		int xlen = intLen;
		int[] qWord = new int[2];
		while (--xlen > 0) {
			long dividendEstimate = (remLong << 32)
					| (value[offset + intLen - xlen] & LONG_MASK);
			if (dividendEstimate >= 0) {
				qWord[0] = (int) (dividendEstimate / divLong);
				qWord[1] = (int) (dividendEstimate - (qWord[0] * divLong));
			} else {
				divWord(qWord, dividendEstimate, divisor);
			}
			quotient.value[intLen - xlen] = (int) qWord[0];
			rem = (int) qWord[1];
			remLong = rem & LONG_MASK;
		}
		// Unnormalize
		if (shift > 0) value[0] = rem %= divisor;
		else value[0] = rem;
		intLen = (value[0] == 0) ? 0 : 1;
		quotient.normalize();
	}
	void divide(MutableBCG b, MutableBCG quotient,
			MutableBCG rem) {
		if (b.intLen == 0)
				throw new ArithmeticException("BCG divide by zero");
		// Dividend is zero
		if (intLen == 0) {
			quotient.intLen = quotient.offset = rem.intLen = rem.offset = 0;
			return;
		}
		int cmp = compare(b);
		// Dividend less than divisor
		if (cmp < 0) {
			quotient.intLen = quotient.offset = 0;
			rem.copyValue(this);
			return;
		}
		// Dividend equal to divisor
		if (cmp == 0) {
			quotient.value[0] = quotient.intLen = 1;
			quotient.offset = rem.intLen = rem.offset = 0;
			return;
		}
		quotient.clear();
		// Special case one word divisor
		if (b.intLen == 1) {
			rem.copyValue(this);
			rem.divideOneWord(b.value[b.offset], quotient);
			return;
		}
		// Copy divisor value to protect divisor
		int[] d = new int[b.intLen];
		for (int i = 0; i < b.intLen; i++)
			d[i] = b.value[b.offset + i];
		int dlen = b.intLen;
		// Remainder starts as dividend with space for a leading zero
		if (rem.value.length < intLen + 1) rem.value = new int[intLen + 1];
		for (int i = 0; i < intLen; i++)
			rem.value[i + 1] = value[i + offset];
		rem.intLen = intLen;
		rem.offset = 1;
		int nlen = rem.intLen;
		// Set the quotient size
		int limit = nlen - dlen + 1;
		if (quotient.value.length < limit) {
			quotient.value = new int[limit];
			quotient.offset = 0;
		}
		quotient.intLen = limit;
		int[] q = quotient.value;
		// D1 normalize the divisor
		int shift = 32 - BCG.bitLen(d[0]);
		if (shift > 0) {
			// First shift will not grow array
			BCG.primitiveLeftShift(d, dlen, shift);
			// But this one might
			rem.leftShift(shift);
		}
		// Must insert leading 0 in rem if its length did not change
		if (rem.intLen == nlen) {
			rem.offset = 0;
			rem.value[0] = 0;
			rem.intLen++;
		}
		int dh = d[0];
		long dhLong = dh & LONG_MASK;
		int dl = d[1];
		int[] qWord = new int[2];
		// D2 Initialize j
		for (int j = 0; j < limit; j++) {
			// D3 Calculate qhat
			// estimate qhat
			int qhat = 0;
			int qrem = 0;
			boolean skipCorrection = false;
			int nh = rem.value[j + rem.offset];
			int nh2 = nh + 0x80000000;
			int nm = rem.value[j + 1 + rem.offset];
			if (nh == dh) {
				qhat = ~0;
				qrem = nh + nm;
				skipCorrection = qrem + 0x80000000 < nh2;
			} else {
				long nChunk = (((long) nh) << 32) | (nm & LONG_MASK);
				if (nChunk >= 0) {
					qhat = (int) (nChunk / dhLong);
					qrem = (int) (nChunk - (qhat * dhLong));
				} else {
					divWord(qWord, nChunk, dh);
					qhat = qWord[0];
					qrem = qWord[1];
				}
			}
			if (qhat == 0) continue;
			if (!skipCorrection) { // Correct qhat
				long nl = rem.value[j + 2 + rem.offset] & LONG_MASK;
				long rs = ((qrem & LONG_MASK) << 32) | nl;
				long estProduct = (dl & LONG_MASK) * (qhat & LONG_MASK);
				if (unsignedLongCompare(estProduct, rs)) {
					qhat--;
					qrem = (int) ((qrem & LONG_MASK) + dhLong);
					if ((qrem & LONG_MASK) >= dhLong) {
						estProduct = (dl & LONG_MASK) * (qhat & LONG_MASK);
						rs = ((qrem & LONG_MASK) << 32) | nl;
						if (unsignedLongCompare(estProduct, rs)) qhat--;
					}
				}
			}
			// D4 Multiply and subtract
			rem.value[j + rem.offset] = 0;
			int borrow = mulsub(rem.value, d, qhat, dlen, j + rem.offset);
			// D5 Test remainder
			if (borrow + 0x80000000 > nh2) {
				// D6 Add back
				divadd(d, rem.value, j + 1 + rem.offset);
				qhat--;
			}
			// Store the quotient digit
			q[j] = qhat;
		} // D7 loop on j
		// D8 Unnormalize
		if (shift > 0) rem.rightShift(shift);
		rem.normalize();
		quotient.normalize();
	}
	private boolean unsignedLongCompare(long one, long two) {
		return (one + Long.MIN_VALUE) > (two + Long.MIN_VALUE);
	}
	private void divWord(int[] result, long n, int d) {
		long dLong = d & LONG_MASK;
		if (dLong == 1) {
			result[0] = (int) n;
			result[1] = 0;
			return;
		}
		// Approximate the quotient and remainder
		long q = (n >>> 1) / (dLong >>> 1);
		long r = n - q * dLong;
		// Correct the approximation
		while (r < 0) {
			r += dLong;
			q--;
		}
		while (r >= dLong) {
			r -= dLong;
			q++;
		}
		// n - q*dlong == r && 0 <= r <dLong, hence we're done.
		result[0] = (int) q;
		result[1] = (int) r;
	}
}

