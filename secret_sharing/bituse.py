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


class bituse:
    """
    Class that manages a string of bits from number of ASCII strings
    It add a leading pad of 0 to reach the desired bit length
    """
    def __init__(self, obj, pad=0):
        """
        Constructs new bituse object from integer,long, or ascii string,
        and if necessary add a leading zeros to match the padding given
        or a multiple of that padding.
        """
        if type(obj) in (int, long):
            self._rep = self._add_pad(bin(obj), pad)
        elif type(obj) is str:
            self._rep = self._s2b(obj)
        else:
            raise TypeError('Does not suport ' + str(type(obj)))

    def _add_pad(self, s, n):
        s = s[2:len(s)]
        if n is not 0:
            for _ in xrange(0, (n - len(s)) % n):
                s = "0" + s
            return s
        else:
            return s

    def _c2b(self, c):
        """
        Char to binary.
        Returns a representation of an ASCII char on 8 bits with leading 0
        """
        ret = bin(ord(c))
        ret = ret[2:len(ret)]
        for i in xrange(len(ret), 8):
            ret = "0" + ret
        return ret

    def _s2b(self, s):
        """
        String to binary.
        Returns a representation of an ASCII string on 8 bits
        for each char with leading 0
        """
        if len(s) is not 0:
            ret = ""
            for i in xrange(0, len(s)):
                ret = ret + self._c2b(s[i])
            return ret
        else:
            return bituse(0, 8).str()

    def block(self, position, blockSize, order=0):
        """
        Split the string into blocks of the given size, and returns the one on the
        required position
        """
        if (position + 1) * blockSize <= self.len(): #ord changes the order of the bits but
            if order is 1:                #returns them in the original order
                s = self._rep[::-1]
                return bituse(long(s[position * blockSize:position * blockSize
                                     + blockSize][::-1], 2), blockSize)
            else:
                s = self._rep
                return bituse(long(s[position * blockSize:position * blockSize
                                     + blockSize], 2), blockSize)
        else:
            raise NameError('Out of range')

    def str(self, ord = 0):
        """
        Get the string representative in binary
        """
        if ord is 0:
            return self._rep
        else:
            return self._rep[::-1]

    def long(self, ord = 0):
        """
        Get the coresponding integer value 
        """
        if ord is 0:
            return long(self._rep, 2)
        else:
            return long(self._rep[::-1], 2)

    def len(self):
        """
        Get the lenght of the bitstring
        """
        return len(self._rep)

    def hex(self, ord = 0):
        """
        Get the hex representation
        """
        if ord is 0:
            ret = hex(long(self._rep, 2))
        else:
            ret = hex(long(self._rep[::-1], 2))
        return ret[2:len(ret) - 1]

    def ascii(self):
        """
        Reverse the initial process of construncting a binary representation
        of a ascii string
        """
        S = ""
        for i in range(0, self.len() / 8):
            S += chr(self.block(i, 8).long())
        return S



