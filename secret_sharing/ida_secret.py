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
import random

import poly
from bituse import *


class ida_secret:
    """
    Class that manages the dealer of a secret sharing scheme with information
    dispersal
    """

    def __init__(self, secret, n, k):
        self.secret = secret      #the secret
        self.n = n                #the number of shares
        self.k = k                #the threshold
        if type(self.secret) in (long, int):
            pass
        elif type(self.secret) is str:
            pad = bituse(self.secret)
            self.secret = pad.long()
        l = len(bin(self.secret)) - 2         #lenghst of secret
        p = l / (k - 1) + (l % (k - 1) != 0)            #lenght of the parts of the secret
        pad = bituse(self.secret, p * (k - 1))     #bit representation
        c = []                              #vector representation
        self.I = []
        c.append(p)                       #first coeficient is the size
        for i in range(0, k - 1):            #split into blocks 
            blk = pad.block(i, p)              #and add to vector
            c.append(blk.long())

        self.P = poly.Poly(c)                    #create Polynomial from vector

    def create_shares(self):
        """
        Creates the shares of the secret using information dispersal
        """
        self.I = [(i, self.P(i)) for i in range(1, self.n + 1)]
        self.av_shares = self.I             #current available shares

    def share_random(self):
        """
        Returns a random part of the secret 
        """
        ret = random.choice(self.av_shares)  #share a random fragment and then remove from list
        self.av_shares.remove(ret)
        return ret

    def _debug(self):
        print "secret= ", self.secret
        print "k= ", self.k
        print "n= ", self.n
        print "P=\n", self.P
        print self.P.c

        print "Shares ----------"

        for i in self.I:
            print i

        print "-----------------"