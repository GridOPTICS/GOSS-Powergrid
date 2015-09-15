import numpy
from pypower.api import ppoption, runpf, printpf
from pprint import pprint
import sys
import json

#ppc = case9()

ppopt = ppoption(PF_ALG=2)

r = runpf(casedata=r'C:\Users\d3m614\git\GOSS-Powergrid\pnnl.goss.syntheticdata\cases\case14',
          ppopt=ppopt)

output = {'baseMVA': r[0]['baseMVA'],
          'branch': r[0]["branch"].tolist(),
          'bus': r[0]["bus"].tolist(),
          'gen': r[0]["gen"].tolist()
          }

print(json.dumps(output))
#for k in r[0].keys():
#    print(k)
#    for j in r[0]:
#        print(j)
    #print(r[0][k])
    #pprint(r[0][k])
##print('R is\n')
##pprint(r)
##print('printpf\n')
##data = {}
##x = r[0]
##for k, v in x.items():
##    if isinstance(v, numpy.ndarray):
##        data[k] = json.dumps(v.tolist())
##    else:
##        data[k] = v
    #data.append(json.dumps(x.tolist()))
#pprint(json.dumps(data))
#printpf(json.dumps(r.tolist()))
