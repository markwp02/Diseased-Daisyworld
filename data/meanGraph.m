function meanGraph

startSteps = [26 26 26 26 26 26 26 26 26 26 26];
endSteps = [106 94 72 70 67 70 62 68 68 64 65];

meanTemp = zeros(1,11);

avg = graph('0.txt','b');
avgHom = avg(startSteps(1):endSteps(1));
meanTemp(1) = mean(avgHom);

avg = graph('0.1.txt','b');
avgHom = avg(startSteps(2):endSteps(2));
meanTemp(2) = mean(avgHom);

avg = graph('0.2.txt','b');
avgHom = avg(startSteps(3):endSteps(3));
meanTemp(3) = mean(avgHom);

avg = graph('0.3.txt','b');
avgHom = avg(startSteps(4):endSteps(4));
meanTemp(4) = mean(avgHom);

avg = graph('0.4.txt','b');
avgHom = avg(startSteps(5):endSteps(5));
meanTemp(5) = mean(avgHom);

avg = graph('0.5.txt','b');
avgHom = avg(startSteps(6):endSteps(6));
meanTemp(6) = mean(avgHom);

avg = graph('0.6.txt','b');
avgHom = avg(startSteps(7):endSteps(7));
meanTemp(7) = mean(avgHom);

avg = graph('0.7.txt','b');
avgHom = avg(startSteps(8):endSteps(8));
meanTemp(8) = mean(avgHom);

avg = graph('0.8.txt','b');
avgHom = avg(startSteps(9):endSteps(9));
meanTemp(9) = mean(avgHom);

avg = graph('0.9.txt','b');
avgHom = avg(startSteps(10):endSteps(10));
meanTemp(10) = mean(avgHom);

avg = graph('1.0.txt','b');
avgHom = avg(startSteps(11):endSteps(11));
meanTemp(11) = mean(avgHom);


meanTemp
infect = [0 0.1 0.2 0.3 0.4 0.5 0.6 0.7 0.8 0.9 1.0];
plot(infect,meanTemp);

title('Mean Temperature of Homeostasis');
xlabel('Infectivity');
ylabel('Global Temperature °C');
end