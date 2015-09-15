import numpy
from pypower.api import ppoption, runpf, printpf
from pprint import pprint
import sys
import json
from os import path

def main(casefile):

    ppopt = ppoption(PF_ALG=2)

    r = runpf(casedata=casefile,
              ppopt=ppopt)

    output = {'baseMVA': r[0]['baseMVA'],
              'branch': r[0]["branch"].tolist(),
              'bus': r[0]["bus"].tolist(),
              'gen': r[0]["gen"].tolist()
          }
    
    sys.stdout.write(json.dumps(output)+'\n')

if __name__ == '__main__':
    if len(sys.argv) != 2:
        sys.stderr.write('Invalid arguments specified\n')
        sys.exit()

    casefile = sys.argv[1]
    if not path.exists(casefile):
        sys.stderr.write('Invalid case file specified\n')
        sys.exit()

    main(casefile)
    
