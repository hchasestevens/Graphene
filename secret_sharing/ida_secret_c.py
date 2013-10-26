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
import poly
from bituse import *


class ida_secret_c:
    """
    Class that manages the client of a secret sharing scheme with information
    dispersal
    """
    def __init__(self):
        self.list = []

    def add_share(self, x):
        """
        Add a new share to the list of this client from a ida_secret dealer
        """
        self.list.append(x)

    def reconstruct(self):
        """
        Reconstruct the secret using lagrange polynomial interpolation
        """
        P = poly.lagrange(self.list)
        return self._extract(P)

    def _extract(self, X):
        """
        Extract the secret from the polynomial
        """
        S = ""
        l = len(X)
        p = long(X[0])        #first coeficient of the polynomial is the parts size

        for i in range(1, l):
            i = long(X[i])
            b = bituse(abs(i), p)
            S = S + b.str()
        ret = long(S, 2)
        return ret
