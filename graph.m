function graph(filename)

fileID = fopen(filename,'r');
line = fgets(fileID);
array = str2num(line);
%display(array);


i = 1;
while line ~= -1
 
    i = i + 1;
    line = fgets(fileID);
    if line ~= -1
        array = [array; str2num(line)];
        %display(line);
    end
end
%line2 = fgets(fileID);
%line3 = fgets(fileID);

%display(array);
%array1 = str2num(line1);
%array2 = str2num(line2);
%array3 = str2num(line3);

dim = size(array);
display(dim);
%for i=1:dim(2)
%    avg(i) = sum(array(i)/10);
%end
%display(sum(array)/10);
avg = sum(array)/dim(1);
x = linspace(1,dim(2),dim(2));
%plot(x,array1,x,array2,x,array3);
plot(x,avg);

end