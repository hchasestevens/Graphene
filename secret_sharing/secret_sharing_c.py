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
from Crypto.Cipher import DES

from shamir_secret_c import shamir_secret_c
from ida_secret_c import ida_secret_c
from bituse import *


class secret_sharing_c:
    """
    Class that implements the dealer side of Krawczyk's technique for
    computational-secure threshold secret sharing scheme. For more information
    about the algorithm go to http://www.springerlink.com/content/0pdlwl2k4n952e2e

    This technique combines Shamir method, with information dispersal and
    a DES encryption.
    
    Example:
    
    from secret_sharing import secret_sharing
    from secret_sharing_c import secret_sharing_c
    
    S = secret_sharing("The quick brown fox jumps over the lazy dog", 100, 6)
    
    s = secret_sharing_c()
    s.add_share(S.share_random())
    s.add_share(S.share_random())
    s.add_share(S.share_random())
    s.add_share(S.share_random())
    s.add_share(S.share_random())
    s.add_share(S.share_random())
    
    print s.reconstruct()
    
    In this case we create a threshold secret sharing scheme with 100 shares,
    and a threshold of 6.
    """

    def __init__(self):
        self.ida = ida_secret_c()
        self.sha = shamir_secret_c()

    def add_share(self, x):
        """
        Add a new share to the list of this client from a dealer
        """
        self.ida.add_share(x[0])
        self.sha.add_share(x[1])

    def reconstruct(self):
        """
        Reconstruct the key and encrypted secret and decrypts the secret
        """
        self.enc = long(self.ida.reconstruct())    #get encrpyted secret
        self.key = long(self.sha.reconstruct())    #get encryption key

        self.enc = long(abs(self.enc))
        self.key = long(abs(self.key))

        self.K = bituse(self.key, 8).ascii()       #get ascii equivalent
        self.E = bituse(self.enc, 8).ascii()

        self.cryp = DES.new(self.K, DES.MODE_ECB)

        return self.cryp.decrypt(self.E)    #decrypt and return

    def _debug(self):
        print "--------------------------------Client"
        print "E     : ", self.E
        print "E  b  : ", self.enc
        print "key   : ", self.K
        print "key b : ", self.key






