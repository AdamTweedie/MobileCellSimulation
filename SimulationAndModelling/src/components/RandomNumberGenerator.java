package components;

import java.util.Random;


public enum RandomNumberGenerator {

    Poisson {
        @Override
        public double getRandom(Random r, double lambda) {
            double L = Math.exp(-lambda);
            int k = 0;
            double p = 1.0;
            do {
                k++;
                p = p * r.nextDouble();
            } while (p > L);

            return k - 1;
        }
    },
    Exponential {
        @Override
        public double getRandom(Random r, double lambda) {
            return -(Math.log(r.nextDouble()) / lambda);
        }
    };

    public abstract double getRandom(Random r, double lambda);
}
