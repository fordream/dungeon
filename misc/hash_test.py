# This script tests several hashing functions to find a good one for the Point class.
#
# using 31   over a small range     got 0.000% hash collision.
# using 31   over a medium range    got 90.169% hash collision.
# using 31   over a large range     got 97.530% hash collision.
# using 193  over a small range     got 0.000% hash collision.
# using 193  over a medium range    got 0.000% hash collision.
# using 193  over a large range     got 6.861% hash collision.
# using 389  over a small range     got 0.000% hash collision.
# using 389  over a medium range    got 0.000% hash collision.
# using 389  over a large range     got 0.000% hash collision.
# using 769  over a small range     got 0.000% hash collision.
# using 769  over a medium range    got 0.000% hash collision.
# using 769  over a large range     got 0.000% hash collision.


class HashFunction(object):
    def __init__(self, function):
        self.function = function

    def __call__(self, x, y, z):
        return self.function(x, y, z)


class SimpleFunction(HashFunction):
    def __init__(self, prime):
        super().__init__(lambda x, y, z: prime * ((prime * x) + y) + z)


class TestCase(object):
    def __init__(self, name, function, x_range, y_range, z_range):
        self.name = name
        self.function = function
        self.ranges = (x_range, y_range, z_range)

    def __call__(self):
        frequency = count_hash_match_frequency(self.function, *self.ranges)
        return '{} got {:.3%} hash collision.'.format(self.name, frequency)


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
    functions = [('using {}'.format(prime), SimpleFunction(prime)) for prime in primes]
    ranges = (('small range', range(-10, 10)), ('medium range', range(-50, 50)), ('large range', range(-100, 100)))
    for hash_function in functions:
        for named_range in ranges:
            name = '{:<10} over a {:<15}'.format(hash_function[0], named_range[0])
            print(TestCase(name, hash_function[1], named_range[1], named_range[1], named_range[1])())
