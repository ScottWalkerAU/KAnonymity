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
pPerAttribute = []
uPerAttribute = []
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
    uPerAttribute.append(utotal/(len(uPerAttribute)+1))
utotal = utotal/(len(utility))
for num in privacy:
    ptotal += num
    pPerAttribute.append(ptotal/(len(pPerAttribute)+1))
ptotal = ptotal/(len(privacy))

plt.figure(figsize=(15,8))
plt.subplot(2,1,2)
plt.ylabel('Utility (%)')
plt.xlabel('Attribute')
plt.title("Dataset Utility per Attribute")
plt.plot(classes,utility)
plt.scatter(classes,utility)

plt.subplot(2,2,1)
plt.ylabel('Utility (%)')
plt.xlabel('Number of Attributes')
plt.title("Dataset Utility per # of Attributes")
plt.axis([0,len(utility),0,100])
z = np.polyfit(range(0,len(uPerAttribute)), uPerAttribute,4)
p = np.poly1d(z)
plt.plot(range(0,len(uPerAttribute)),p(range(0,len(uPerAttribute))),'r:')

plt.subplot(2,2,2)
plt.ylabel('Privacy (%)')
plt.xlabel('Number of Attributes')
plt.title("Dataset Privacy per # of Attributes")
plt.axis([0,len(privacy),0,100])
z = np.polyfit(range(0,len(pPerAttribute)), pPerAttribute,4)
p = np.poly1d(z)
plt.plot(range(0,len(pPerAttribute)),p(range(0,len(pPerAttribute))),'r:')
plt.plot(range(0,len(classes)),p(range(0,len(classes))),'b:')

plt.show()