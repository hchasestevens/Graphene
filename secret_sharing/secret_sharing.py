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

from shamir_secret import shamir_secret
from ida_secret import ida_secret
import prime
from bituse import *


class secret_sharing:
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

    def __init__(self, secret, n, k):
        """
        Create the shares of the secret using  Krawczyk's technique for
        computational-secure threshold secret sharing scheme with n shares
        and with a k threshold
        """
        self.secret = self._pad(secret)                    #the secret    
        self.key = prime.rand_ascii(8);                  #generate random key

        self.cryp = DES.new(self.key, DES.MODE_ECB)
        self.S = self.cryp.encrypt(self.secret);      #use key to encrypt message

        self.ida_sh = ida_secret(self.S, n, k)        #create new secret sharing object with information dispersal
        self.sha_sh = shamir_secret(self.key, n, k)   #create new secret sharing object with Shamir's Scheme

        self.ida_sh.create_shares()                 #create respective shares
        self.sha_sh.create_shares()

    def share_random(self):
        """
        Returns a random part of the secret 
        """
        return self.ida_sh.share_random(), self.sha_sh.share_random()

    def _pad(self, m):
        if len(m) % 8 is not 0:
            for _ in range(0, 8 - len(m) % 8):
                m += chr(0)
        return m

    def _debug(self):
        print "--------------------------------Dealer"
        print "Secret: ", self.secret
        print "S     : ", self.S
        print "S   b : ", bituse(self.S).long()
        print "key   : ", self.key
        print "key b : ", bituse(self.key).long()
        #self.ida_sh._debug()
        #self.sha_sh._debug()

