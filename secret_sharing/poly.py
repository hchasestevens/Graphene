#!/usr/bin/python
# -*- coding: utf-8 -*-
###############################################################################
#    Copyright (C) 2012 Chirila Alexandru
###############################################################################
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.
###############################################################################
from copy import deepcopy


class Poly(list):

    """Class that implements polynomials and different operation between them.

    This class actually extends the built-in list class, by overriding some of
    the methods. Therefore an instance can be created from any iterable object.
    For the class to work the contained objects should have defined the add,
    sub, mul, div, mod and pow operations. Every operation explains in their
    docs what methods you should have to implement.

    The list represents the coefficients of the polynomial, starting from x^0
    (free coefficient) to x^n (n the rank of the polynomial).

    Example:
        >>> poly.Poly([1,2,3,4])
                   2    3
        1 + 2x + 3x + 4x
        >>> poly.Poly([1,None,None,None,-1])
                4
        1 + -1x

    Also because the class is also a list, all methods available for a list is
    also available for a polynomial, such as append, slice or getitem etc.

    The goal of this implementations is to be easily used with any other
    numeric like class as coefficients of the polynomial. For example you
    can easily use fractions, long integers, decimal, complex numbers without
    writing any extra code.

    These operations will work with other iterable objects and single items
    (scalars).

    Because the class tries to be flexible, the null value of every operation
    will not be 0 but will be None. For this to work the objects should have
    the unary operator neg defined, also the eq test for equality.

    If those methods are implemented in every operation, the None values will
    be ignored. For example if we take 2 polynomials : [1,5,7] and [-1,2],
    the sum will be [None,7,7].

    An instance can also be called as a function (with one argument), the
    result being the value of the polynomial in that point. For this to work
    the contained objects should have the pow operation defined.
    The modulo version is also suported for calling a function (with two
    arguments), then the pow version should be defined for the contained
    object. If the argument sent when calling the object is iterable, it will
    be assumed that it is a polynomial and therefore it will return a new
    polynomial from the original composed with the argument  ( f(g(x)) ).

        Example:
            >>> p = Poly([2,5])
            >>> p(3)
            17
            >>> p(3, 5)
            2
            >>> p.horner(3)
            17

    The value can also be calculated using the horner method.

    So as long as the special methods are defined you can use any class as a
    coefficient for your polynomial.

    """

    def __init__(self, *args, **kwargs):
        list.__init__(self, *args, **kwargs)

    def __repr__(self):
        return self.__str__()

    def __str__(self):
        """Returns string representation of polynomial.

        Coefficients must have __str__ defined.

        """
        p = 0
        S = ""
        P = ""
        for i in self:
            if i != None:
                if p == 0:
                    P += " " * (len(str(i)) + 2)
                    S += str(i)
                elif p == 1:
                    P += " " * (len(str(i)) + 3)
                    S += str(i) + "x"
                else:
                    P += " " * (len(str(i)) + 3) + str(p)
                    S += str(i) + "x"
                if p != len(self) - 1:
                    S += " + "
            p += 1
        return P + "\n" + S

    def __nonzero__(self):
        """Calls and returns is_null() negated."""
        return not self.is_null()

    def __eq__(self, other):
        """Tests equality between two polynomials

        Coefficients must have __ne__ and __eq__ defined.

        """
        o = deepcopy(other)
        s = deepcopy(self)
        o.trim()
        s.trim()
        if len(o) == len(s):
            for i in range(0, len(o)):
                if o[i] != s[i]:
                    return False
            return True
        else:
            return False

    def __ne__(self, other):
        """Tests inequality between two polynomials

        Coefficients must have __ne__ and __eq__ defined.

        """
        o = deepcopy(other)
        s = deepcopy(self)
        o.trim()
        s.trim()
        if len(o) == len(s):
            for i in range(0, len(o)):
                if o[i] != s[i]:
                    return True
            return False
        else:
            return True

    def __add__(self, other):
        """Adds to self polynomial or scalar.

        Coefficients must have __add__, __neg__ and __eq__ defined.
        If this operation results in simplification of terms, the
        corresponding terms in the returned polynomial are replaced
        with None.

        """
        if hasattr(other, "__iter__"):
            if len(self) >= len(other):
                c = deepcopy(self)
                f = deepcopy(other)
            else:
                c = deepcopy(other)
                f = deepcopy(self)
            for i in range(0, len(f)):
                if c[i] != None and f[i] != None:
                    if c[i] == -f[i]:
                        c[i] = None
                    else:
                        c[i] += f[i]
                elif c[i] == None and f[i] != None:
                    c[i] = f[i]
            return Poly(c)
        else:
            c = deepcopy(self)
            if c[0] == -other:
                c[0] = None
            else:
                c[0] += other
            return c

    def __sub__(self, other):
        """Subtracts to self polynomial or scalar.

        Coefficients must have __sub__, __neg__ and __eq__ defined.
        If this operation results in simplification of terms, the
        corresponding terms in the returned polynomial are replaced
        with None.

        """
        if hasattr(other, "__iter__"):
            if len(self) >= len(other):
                c = deepcopy(self)
                f = deepcopy(other)
            else:
                c = deepcopy(other)
                f = deepcopy(self)
            for i in range(0, len(f)):
                if c[i] != None and f[i] != None:
                    if c[i] == f[i]:
                        c[i] = None
                    else:
                        c[i] -= f[i]
                elif c[i] == None and f[i] != None:
                    c[i] = -f[i]
            return Poly(c)
        else:
            c = deepcopy(self)
            if c[0] == other:
                c[0] = None
            else:
                c[0] -= other
            return c

    def __neg__(self):
        """Applies - (unary) to all of the polynomial's coefficients

        Coefficients must have __neg__ defined.

        """
        ret = deepcopy(self)
        for i in range(0, len(ret)):
            ret[i] = -ret[i]
        return ret

    def __pos__(self):
        """Applies + (unary) to all of the polynomial's coefficients

        Coefficients must have __pos__ defined.

        """
        ret = deepcopy(self)
        for i in range(0, len(ret)):
            ret[i] = +ret[i]
        return ret

    def __abs__(self):
        """Applies abs() to all of the polynomial's coefficients

        Coefficients must have __abs__ defined.

        """
        ret = deepcopy(self)
        ret.cast(abs)
        return ret

    def __mul__(self, other):  # multiplying with polynomial or scalar
        """Multiplies self with polynomial or scalar.

        Coefficients must have __mul__, __add__, __neg__ and __eq__ defined.
        If this operation results in simplification of terms, the
        corresponding terms in the returned polynomial are replaced
        with None.

        """
        if hasattr(other, "__iter__"):
            ret = Poly()
            for i in range(0, len(self)):
                if self[i] != None:
                    new_poly = deepcopy(other)
                    for j in range(0, len(other)):
                        if new_poly[j] != None:
                            new_poly[j] *= self[i]
                    for j in range(0, i):
                        new_poly.insert(0, None)
                    ret = deepcopy(ret + new_poly)
        else:
            ret = deepcopy(self)
            for i in range(0, len(ret)):
                if ret[i] != None:
                    ret[i] *= other
        return ret

    def __pow__(self, other, modulo=None):
        """Multiplies self with polynomial or scalar.

        Coefficients must have __pow__,__mul__, __add__, __neg__
        and __eq__ defined. If this operation results in simplification
        of terms, the corresponding terms in the returned polynomial are
        replaced with None.

        Also it very important that the coefficients have __pow__ and
        specifically a case for pow(coeff, 0).

        For the modulo version __mod__ is needed.

        """
        if isinstance(other, int):
            if other == 0:
                ret = Poly([self[self.rank() - 1] ** 0])
                return ret
            ret = deepcopy(self)
            if modulo != None:
                for _ in range(1, other):
                    ret = (ret * self) % modulo
            else:
                for _ in range(1, other):
                    ret = ret * self
            return ret
        else:
            raise TypeError("Type " + str(type(other)) +
                            " not supported, integer only")

    def __mod__(self, other):
        """Divides self with polynomial or scalar and returns the remainder.

        Coefficients must have __mul__, __sub__, __neg__ and __eq__ defined.
        If this operation results in simplification of terms, the
        corresponding terms in the returned polynomial are replaced
        with None.

        """
        if hasattr(other, "__iter__"):
            other = Poly(other)
            if len(other) != 0:
                remainder = deepcopy(self)
                quotient = Poly()
                while remainder.rank() >= other.rank():
                    coeff = remainder[remainder.rank()] / other[other.rank()]
                    power = remainder.rank() - other.rank()
                    quotient.add_term(coeff, power)
                    remainder -= Poly().add_term(coeff, power) * other
                return remainder
            else:
                raise ZeroDivisionError('Cannot divide by null polynomial')
        else:
            ret = deepcopy(self)
            for i in range(0, len(ret)):
                if ret[i] != None:
                    ret[i] = ret[i] % other
            return ret

    def __div__(self, other):  # dividing a polynomial with a scalar
        """Divides self with polynomial or scalar and returns the quotient.

        Coefficients must have __mul__, __sub__, __neg__ and __eq__ defined.
        If this operation results in simplification of terms, the
        corresponding terms in the returned polynomial are replaced
        with None.

        """
        if hasattr(other, "__iter__"):
            other = Poly(other)
            if len(other) != 0:
                remainder = deepcopy(self)
                quotient = Poly()
                while remainder.rank() >= other.rank():
                    coeff = remainder[remainder.rank()] / other[other.rank()]
                    power = remainder.rank() - other.rank()
                    quotient.add_term(coeff, power)
                    remainder -= Poly().add_term(coeff, power) * other
                return quotient
            else:

                raise ZeroDivisionError('Cannot divide by nul polynomial')
        else:
            ret = deepcopy(self)
            for i in range(0, len(ret)):
                if ret[i] != None:
                    ret[i] = ret[i] / other
            return ret

    def __divmod__(self, other):
        """Divides self with polynomial or scalar and returns the
        quotient and the remainder.

        Coefficients must have __mul__, __sub__, __neg__ and __eq__ defined.
        If this operation results in simplification of terms, the
        corresponding terms in the returned polynomial are replaced
        with None.

        """
        if hasattr(other, "__iter__"):
            other = Poly(other)
            if len(other) != 0:
                remainder = deepcopy(self)
                quotient = Poly()
                while remainder.rank() >= other.rank():
                    coeff = remainder[remainder.rank()] / other[other.rank()]
                    power = remainder.rank() - other.rank()
                    quotient.add_term(coeff, power)
                    remainder -= Poly().add_term(coeff, power) * other
                return (quotient, remainder)
            else:

                raise ZeroDivisionError('Cannot divide by null polynomial')
        else:
            quotient = deepcopy(self)
            remainder = deepcopy(self)
            for i in range(0, len(self)):
                if self[i] != None:
                    quotient[i] /= other
                    remainder[i] &= other
            return (quotient, remainder)

    def __call__(self, x, modulo=None):
        """Calculate the value of the polynomial in x.

        Coefficients need __add__ and __pow__.

        The result is calculated with a direct evaluation.

        For better performance use horner method

        """
        if hasattr(x, "__iter__"):
            if modulo != None:
                return self.compose(x) % modulo
            else:
                return self.compose(x)
        deg = self.lowest_deg()

        if modulo != None:
            S = (self[deg] * pow(x, deg, modulo)) % modulo
            for i in range(deg + 1, len(self)):
                if self[i] != None:
                    S += (self[i] * pow(x, i, modulo)) % modulo
            return S % modulo
        else:
            S = self[deg] * pow(x, deg)
            for i in range(deg + 1, len(self)):
                if self[i] != None:
                    S += self[i] * pow(x, i)
            return S

    def compose(self, poly):
        """Compose polynomial with the given one

        Returns new polynomial equal to f(g(x)), where f is self, and g
        the given polynomial.

        poly -- Polynomial or any iterable type.

        """
        if not hasattr(poly, "__iter__"):
            raise TypeError('Must be iterable')
        else:
            poly = Poly(poly)
            ret = Poly()
            comp = pow(poly, 0)
            for term in self:
                ret = ret + term * comp
                comp *= poly
            return ret

    def lowest_deg(self):
        """Return the degree of the lowest term."""

        for i in range(0, len(self)):
            if self[i] != None:
                return i

    def highest_deg(self):
        """Return the degree of the highest term (the rank of the polynomial).
        """
        for i in reversed(range(0, len(self))):
            if self[i] != None:
                return i

    def rank(self):
        """Return the rank of the polynomial."""
        return self.highest_deg()

    def add_term(self, coeff, power):
        """Adds a new term to the polynomial.

        If a term with the same power exists it will simply add the coeff to
        the existing one. Else, if necessary the list will be filled with
        None items to achieve the new rank. All None items are ignored in
        every operation defined by the polynomial class.

        Returns self.

        coeff -- coefficient of the new term
        power -- the degree of the new term

        """
        if len(self) > power:
            if self[power] != None:
                self[power] += coeff
            else:
                self[power] = coeff
        else:
            while len(self) < power:
                self.append(None)
            self.append(coeff)
        return self

    def horner(self, x):
        """Uses the Horner method to calculate the value of the polynomial in
        the given point."""
        if hasattr(x, "__iter__"):
            raise TypeError('Must be non-iterable')
        self.trim(None)
        ret = self[len(self) - 1]
        for i in reversed(range(0, len(self) - 1)):
            if self[i] != None:
                ret = ret * x + self[i]
            else:
                ret = ret * x
        return ret

    def cast(self, spell):
        """Calls the given function on every coefficient of the polynomial.

        Returns self.

        spell -- name of a method (example: float,int)
        """
        if not hasattr(spell, "__call__"):
            raise TypeError('Must be callable')
        for i in range(0, len(self)):
            if self[i] != None:
                self[i] = spell(self[i])
        return self

    def _der(self):
        del self[0]
        for i in range(0, len(self)):
            if self[i] != None:
                self[i] *= i + 1

    def derivative(self, number=1):
        """Returns the derivative of rank given as argument of self in a new
        polynomial.

            number -- number of derivative taken, must be
                      non-negative (default 1).

        """
        if number < 0:
            raise ValueError('Number of derivative must be positive')
        ret = deepcopy(self)
        while number != 0:
            ret._der()
            number -= 1
        return ret

    def _itg(self):
        self.insert(0, None)
        for i in range(0, len(self)):
            if self[i] != None:
                self[i] /= i

    def integral(self, number=1):
        """Returns the indefinite integral of rank given as argument of self
        in a new polynomial.

           The constant resulted as lower bound is None, therefore will be
           ignored.

           number -- number of derivative taken, must be non-negative
                       (default 1).

        """
        if number < 0:
            raise ValueError('The number of integrations must be positive')
        ret = deepcopy(self)
        while number != 0:
            ret._itg()
            number -= 1
        return ret

    def trim(self, zero=0):
        """Removes any leading None or null values (does not remove any value
        from the interior of the polynomial, only the leading ones).

        zero -- your own defined null value to be removed (default 0)

        """
        for i in reversed(range(0, len(self))):
            if self[i] != None and self[i] != zero:
                break
            else:
                del self[i]
        return self

    def is_null(self, zero=0):
        """Returns True if polynomial is null (f(x)=0 or f(x)=None or [])
        and False otherwise.

        zero -- your own defined null value to be considered (default 0)

        """
        for i in self:
            if i != None and i != zero:
                return False
        return True

    def newton(self, start, top_bound, zero=0):
        """Not stable, stable patch comming soon."""
        der = self.derivative()
        root = start
        if top_bound < 0:
            raise ValueError('The number of tries cannot be negative.')
        while top_bound != 0:
            value1 = self.horner(root)
            value2 = der.horner(root)
            if zero in [value1, value2]:
                break
            if abs(zero - value1) < 0.0001:
                break
            root = root - value1 / value2
            top_bound -= 1

        return root

    def get_roots(self):
        if self.rank() == 0:
            return []
        elif self.rank() == 1:
            return self._solve_1()
        elif self.rank() == 2:
            return self._solve_2()
        elif self.rank() == 3:
            return self._solve_3()
        elif self.rank() == 4:
            return self._solve_4()
        else:
            return self._solve_more()

    def _solve_1(self):
        if self[0] != None:
            return [-self[0] / self[1]]
        else:
            # Necessary if the coefficients are not Python's numeric native
            return self[1] - self[1]

    def _solve_2(self):
        pass

    def _solve_3(self):
        pass

    def _solve_4(self):
        pass

    def _solve_more(self):
        pass

from fractions import Fraction


def lagrange(points):
    """Applies the LaGrange polynomial interpolation and returns the
    reconstructed polynomial. Currently only works with numerical values
    defined natively in Python.

    The function uses the Poly class for representing polynomials and Fractions
    as coefficients of the returned polynomial. From there any cast can be
    applied to reach a desirable form using the cast method from the Poly class

    points -- a points of points on the graph of the polynomial
                ([[x,f(x)],[y,f(y)]...])

        Example:
            >>> p = poly.lagrange([[2,1942],[4,3402],[5,4414]])
            >>> print p
                             2
            1234 + 166x + 94x

    """
    ret = Poly()
    for i in points:
        n = Poly([Fraction(i[1])])
        d = Fraction(1)
        for j in points:
            if j is not i:
                n = n * Poly([Fraction(-j[0]), Fraction(1)])
                d *= Fraction(i[0] - j[0])
        p = n / d
        ret = ret + p
    return ret
