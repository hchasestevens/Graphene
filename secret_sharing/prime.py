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
"""
    Set of functions that work with large random prime numbers
and different utilities
"""
import random


def rand_prime(bits):	#generate random prime on specific number of bits
    ok = 0
    #nr = 0
    while ok is 0:
        p = random.randrange(pow(2, bits - 1), pow(2, bits) - 1, 2) - 1   #random odd number
        ok = miller_rabin(p, 10)                     #check primality
       #nr += 1
    #print "\t", nr, "tries"
    return p


def rand(bits):			#generate random number on specific number of bits
    return random.randrange(pow(2, bits - 1), pow(2, bits) - 1)


def rand_ascii(k):
    ret = ""
    for i in range(0, k):
        ret += chr(random.choice(range(1, 256)))
    return ret


def imod(x, n):     #Modular Inverse of x in Zn
    return eea(x, n)[0] % n


def eea(u, v):     #extended Euclid Algorithm
    u1 = 1
    u2 = 0
    u3 = u
    v1 = 0
    v2 = 1
    v3 = v
    ls = []
    while v3 != 0:
        q = u3 / v3
        ls.append(q)
        t1 = u1 - q * v1
        t2 = u2 - q * v2
        t3 = u3 - q * v3
        u1 = v1
        u2 = v2
        u3 = v3
        v1 = t1
        v2 = t2
        v3 = t3
    return u1, u2, u3, ls


def miller_rabin(n, k): #miller_rabin test for primality	
    s = 0					#n - number , k - the number of tries
    d = n - 1
    while 1:
        if d % 2 == 0:
            s += 1
            d = d / 2
        else:
            break

    for _ in range(1, k):
        ok = 0
        a = random.randint(2, n - 2)
        x = pow(a, d, n)
        if x == 1 or x == n - 1:
            continue
        else:
            for _ in range(1, s - 1):
                x = pow(x, 2, n)
                if x == 1:
                    return 0
                if x == n - 1:
                    ok = 1
                    break
            if (ok == 0):
                return 0
            else:
                continue
    return 1
