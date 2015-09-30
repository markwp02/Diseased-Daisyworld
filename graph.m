function  graph(filename,colour)

fileID = fopen(filename,'r');
line = fgets(fileID);
array = str2num(line);
display(size(array));


i = 1;
while line ~= -1
 
    i = i + 1;
    line = fgets(fileID);
    
    if line ~= -1
       display(size(str2num(line)));
       array = [array; str2num(line)];
        
    end
end
%line2 = fgets(fileID);
%line3 = fgets(fileID);

%display(array);
%array1 = str2num(line1);
%array2 = str2num(line2);
%array3 = str2num(line3);

dim = size(array);
%display(dim);
%for i=1:dim(2)
%    avg(i) = sum(array(i)/10);
%end
%display(sum(array)/10);
%avg = sum(array)/dim(1);
avg = mean(array);
e = std(array);
L = avg - min(array);
U = max(array) - avg;
x = linspace(1,dim(2),dim(2));
%plot(x,array1,x,array2,x,array3);
%plot(x,U);
hErrBar = errorbar(x,avg,L,U,colour);
hb = get(hErrBar,'children');  
Xdata = get(hb(2),'Xdata');

temp = 4:3:length(Xdata);
temp(3:3:end) = [];

xleft = temp; xright = temp+1;
Xdata(xleft) = Xdata(xleft) + 1.3;
Xdata(xright) = Xdata(xright) - 1.3;

%// Update
set(hb(2),'Xdata',Xdata)
end