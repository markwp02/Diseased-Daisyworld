function graph
fileID = fopen('filename.txt','r');
line = fgets(fileID);
array = str2num(line);
dim = size(array);
x = linspace(1,dim(2),dim(2));
plot(x,array);

end