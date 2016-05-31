import java.math.BigInteger;

class Matrix2 {
	public BigInteger a;
	public BigInteger b;
	public BigInteger c;
	public BigInteger d;

	public Matrix2(int a, int b, int c, int d) {
		this.a = BigInteger.valueOf(a);
		this.b = BigInteger.valueOf(b);
		this.c = BigInteger.valueOf(c);
		this.d = BigInteger.valueOf(d);
	}

	public Matrix2(BigInteger a, BigInteger b, BigInteger c, BigInteger d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	public Matrix2 multiply(Matrix2 multiplier) {
		return new Matrix2(this.a.multiply(multiplier.a).add(this.b.multiply(multiplier.c)),
		                   this.a.multiply(multiplier.b).add(this.b.multiply(multiplier.d)),
		                   this.c.multiply(multiplier.a).add(this.d.multiply(multiplier.c)),
		                   this.c.multiply(multiplier.b).add(this.d.multiply(multiplier.d)));
	}
}

public class LogPow {
	public static BigInteger fibonacci(int n) {
		// ( F_n  ) = ( 0  1 )^n  * ( F_0 )		
		// ( F_n+1)   ( 1  1 )      ( F_1 )
		Matrix2 currentMultiplier = new Matrix2(0, 1, 1, 1); // Fibonacci matrix
		Matrix2 result = new Matrix2(1, 0, 0, 1); // Identity matrix
		int currentMask = 1;
		int power = n - 1;
		while (currentMask <= power) {
			if ((currentMask & power) != 0) {
				result = result.multiply(currentMultiplier);
			}
			currentMask <<= 1;
			currentMultiplier = currentMultiplier.multiply(currentMultiplier);			
		}
		return result.d;
	}

	public static BigInteger logPow(int base, int power) {
		BigInteger currentMultiplier = BigInteger.valueOf(base);
		BigInteger result = BigInteger.valueOf(1);	
		int currentMask = 1;
		while (currentMask <= power) {
			if ((currentMask & power) != 0) {
				result = result.multiply(currentMultiplier);
			}
			currentMask <<= 1;
			currentMultiplier = currentMultiplier.multiply(currentMultiplier);			
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(String.valueOf(logPow(5, 120)));
		System.out.println();
		System.out.println(String.valueOf(fibonacci(1_000_000)));
	}
}
