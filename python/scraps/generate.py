import json
import string
import random
import base64

def random_char(y):
    return ''.join(random.choice(string.ascii_letters) for x in range(y))

with open("image.jpg", "r") as f:
    image = f.read()
    
small_text = random_char(100)
medium_text = random_char(1000)
long_text = random_char(5000)
data_values = (small_text, medium_text, long_text, image)

data = {}
data64 = {}
for key_id in xrange(20):
    key = random_char(128)
    data[key] = ["repeat,.jpg"]
    data64[key] = ["repeat,.jpg"]
    for i in xrange(random.randint(5, 20)):
        value = random.choice(data_values)
        if value == image:
            data64[key].append(base64.encodestring(value))
            data[key].append(value.replace("\n","!@#$%^&*()"))
            data[key].append(value.replace("\r",")(*&^%$#@!"))
        else:
            data[key].append(value)
            data64[key].append(value)
            
json.dump(data64, open("sample.json", "w"))

with open("sample.txt", "wb") as output:
    writeln = lambda x: output.write(x + "\n")
    for key, values in data.iteritems():
        writeln(key)
        for value in values:
            writeln("\t" + value)
    