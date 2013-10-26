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
