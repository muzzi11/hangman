buckets = []
for i in range(24):
    buckets.append([])

with open("words.txt") as f:
    lines = f.readlines()    
    
    for line in lines:
        line = line.strip()
        length = len(line)
        buckets[length - 1].append(line)
        
for i in range(1, 25):
    with open("words" + str(i) + ".txt", "wb+") as f:
        for word in buckets[i - 1]:
            f.write(word + "\n");
        
            

        
        