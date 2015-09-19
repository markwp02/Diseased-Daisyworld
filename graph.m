function graph
fileID = fopen('filename.txt','r');
line1 = fgets(fileID);
%line2 = fgets(fileID);
%line3 = fgets(fileID);

%display(line);
array1 = str2num(line1);
%array2 = str2num(line2);
%array3 = str2num(line3);
dim = size(array1);
x = linspace(1,dim(2),dim(2));
%plot(x,array1,x,array2,x,array3);
plot(x,array1);

end