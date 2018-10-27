import argparse
import matplotlib.pyplot as plt
import numpy as np
import csv

parser = argparse.ArgumentParser()
parser.add_argument('--file', type=str, default='statistics.csv', help='file with data')
opt = parser.parse_args()
print(opt)

classes = []
utility = []
privacy = []
utotal = 0
ptotal = 0

with open(opt.file, 'r') as f:
	reader = csv.reader(f)
	for row in reader:
		classes.append(row[0])
		utility.append(float(row[1]))
		privacy.append((100-float(row[1])))
for num in utility:
    utotal += num
utotal = utotal/(len(utility))
for num in privacy:
    ptotal += num
ptotal = ptotal/(len(privacy))

plt.figure(figsize=(12,7))
plt.plot(classes,utility)
plt.scatter(classes,utility)
a = np.arange(100,utotal,-((100-utotal)/len(utility)))
z = np.polyfit(range(0,len(classes)), a.tolist(),2)
p = np.poly1d(z)
plt.plot(range(0,len(classes)),p(range(0,len(classes))),'r:')

b = np.arange(0,ptotal,((0+ptotal)/len(privacy)))
z = np.polyfit(range(0,len(classes)), b.tolist(),2)
p = np.poly1d(z)
plt.plot(range(0,len(classes)),p(range(0,len(classes))),'b:')

plt.ylabel('Utility (%)')
plt.xlabel('Attribute')
plt.title("Dataset Utility vs Privacy")
plt.legend(['Utility per attribute','Overall Utility Loss','Overall Privacy Gain'])

plt.show()