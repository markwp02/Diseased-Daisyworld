function showGraphs
hold on
%graph('0.txt', 'b');
%graph('0.1.txt',[0 .25 1]);
%graph('0.1.txt','c');
%graph('0.2.txt',[0.5 0 1]);
%graph('0.3.txt', [0 0.5 0.5]);
%graph('0.4.txt', 'r');
%graph('0.4.txt', [0 1 0]);
%graph('0.5.txt', [0.75 0.75 0]);
%graph('0.5.txt', 'g');
%graph('0.6.txt', 'c');
%graph('0.7.txt','b');
%graph('0.7.txt',[0.5 0 1]);

graph('0.8.txt','r');
graph('0.9.txt','g');
%graph('1.0.txt',[0 0 0]);
graph('1.0.txt','c');


title('Global Temperature for varying infectivity');
ylabel('Global temperature °C');
xlabel('Number of steps');
hold off

end