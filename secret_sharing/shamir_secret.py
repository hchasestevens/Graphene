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
import prime
from bituse import *


class shamir_secret:
    """
    Class that manages the dealer of a Shamir secret sharing scheme
    """
    def __init__(self, obj, n, k, pad=256):
        self.field = prime.rand_prime(pad)  #the field 
        #self.field=obj
        self.secret = obj               #the secret we want to share
        self.k = k                    #the threshold
        self.n = n                    #number of shares
        c = []                        #the Polynomial

        if type(self.secret) in (long, int):
            pass
        elif type(self.secret) is str:  #if secret is string transform to long
            pad = bituse(self.secret)
            self.secret = pad.long()
            #self.field=pad.long()

        c.append(self.secret)           #append secret as free coeficient

        for _ in range(1, k):            #generate random Polynomial coeficients
            c.append(random.randrange(2, self.field))

        self.P = poly.Poly(c)                  #create Polynomial

    def create_shares(self):
        self.I = [(i, self.P(i)) for i in range(1, self.n + 1)]
        self.av_shares = self.I           #current available shares

    def share_random(self):
        ret = random.choice(self.av_shares)  #share a random fragment and then remove from list
        self.av_shares.remove(ret)
        return ret

    def _debug(self):
        print "field=", self.field
        print "secret= ", self.secret
        print "k= ", self.k
        print "n= ", self.n
        print "P -------------"
        print self.P
        print self.P.c
        print "----------------"
        print "I -------------"
        for i in self.I:
            print i
        print "----------------"




