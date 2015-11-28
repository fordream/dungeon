# This script tests several hashing functions to find a good one for the Point class.


class HashFunction(object):
    def __init__(self, function):
        self.function = function

    def __call__(self, x, y, z):
        return self.function(x, y, z)


class SimpleFunction(HashFunction):
    def __init__(self, prime):
        super().__init__(lambda x, y, z: prime * ((prime * x) + y) + z)


def count_hash_match_frequency(f, x_range, y_range, z_range):
    total_hashes = 0
    hash_set = set()
    for x in x_range:
        for y in y_range:
            for z in z_range:
                hash_set.add(f(x, y, z))
                total_hashes += 1
    return 1 - len(hash_set) / total_hashes


if __name__ == '__main__':
    primes = [31, 193, 389, 769]
    functions = [SimpleFunction(prime) for prime in primes]
    for hash_function in functions:
        print(count_hash_match_frequency(hash_function, range(-50, 50), range(-50, 50), range(-50, 50)))
