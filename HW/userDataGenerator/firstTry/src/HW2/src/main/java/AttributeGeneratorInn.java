// https://ru.wikipedia.org/wiki/%D0%9A%D0%BE%D0%BD%D1%82%D1%80%D0%BE%D0%BB%D1%8C%D0%BD%D0%BE%D0%B5_%D1%87%D0%B8%D1%81%D0%BB%D0%BE#%D0%9D%D0%BE%D0%BC%D0%B5%D1%80%D0%B0_%D0%98%D0%9D%D0%9D
// https://toster.ru/q/72016


public class AttributeGeneratorInn extends AttributeGenerator {

    private int[] innNumbersPrefix = {7, 7};
    private int length = 12;  // 10 is also valid (for legal entities) but here not implemented, YAGNI :)
    private int checksumNumbersCount = 2;

    public AttributeGeneratorInn() { super(); }

    @Override
    public Object generate() {
        int[] innNumbersMeaningfulExceptPrefix;
        int[] innNumbersMeaningfulWithPrefix;
        int[] innNumbersChecksum;
        int[] innNumbersCombined;

        innNumbersMeaningfulExceptPrefix = this.getMeaningfulNumbersExceptPrefixRandomly();
        innNumbersMeaningfulWithPrefix = Utils.concatenateArraysInt(this.innNumbersPrefix, innNumbersMeaningfulExceptPrefix);

        innNumbersChecksum = this.getChecksumNumbersGivenMeaningfulOnes(innNumbersMeaningfulWithPrefix);
        innNumbersCombined = Utils.concatenateArraysInt(innNumbersMeaningfulWithPrefix, innNumbersChecksum);

        AttributeInn result = new AttributeInn(innNumbersCombined);

        return (Object) result;
    }

    private int[] getMeaningfulNumbersExceptPrefixRandomly() {
        int[] innNumbersMeaningfulExceptPrefix = new int[this.length - this.innNumbersPrefix.length - this.checksumNumbersCount];

        for (int i = 0; i < innNumbersMeaningfulExceptPrefix.length; i++) {
            innNumbersMeaningfulExceptPrefix[i] = Utils.randomInt(10);
        }

        return innNumbersMeaningfulExceptPrefix;
    }

    private int[] getChecksumNumbersGivenMeaningfulOnes(int[] innNumbersMeaningful) {
        int[] innNumbersChecksum = new int[this.checksumNumbersCount];

        for (int i = 0; i < this.checksumNumbersCount; i++) {
            innNumbersChecksum[i] = this.getChecksumNumber(innNumbersMeaningful, innNumbersChecksum, i);
        }

        return innNumbersChecksum;
    }

    private int getChecksumNumber(int[] innNumbers, int[] innNumbersChecksum, int checksumNumberRelativeIndex) {
        if (checksumNumberRelativeIndex == 0) {
            return this.getLastButOneNumber(innNumbers);
        } else if (checksumNumberRelativeIndex == 1) {
            return this.getLastNumber(innNumbers, innNumbersChecksum[0]);
        } else {
            throw new IllegalArgumentException(
                    String.format("Invalid checksum number index \"%s\"", checksumNumberRelativeIndex));
        }
    }

    private int getLastButOneNumber(int[] innNumbers) {
        int[] factors = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};

        return this.getModuloForChecksum(
                Utils.multiplyArraysInt(factors, innNumbers)
        );
    }

    private int getLastNumber(int[] innNumbers, int lastButOneChecksumNumber) {
        int[] factors = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6};
        int factorForChecksumNumber = 8;

        return this.getModuloForChecksum(
                Utils.multiplyArraysInt(innNumbers, factors) +
                        factorForChecksumNumber * lastButOneChecksumNumber
        );
    }

    private int getModuloForChecksum(int number) {
        return number % 11 % 10;
    }
}
