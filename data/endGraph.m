function endGraph

endSteps = [106 94 72 70 67 70 62 68 68 64 65];
infect = [0 0.1 0.2 0.3 0.4 0.5 0.6 0.7 0.8 0.9 1.0];

plot(infect, endSteps);
title('End of Homeostasis');
xlabel('Infectivity');
ylabel('Number of steps');
end