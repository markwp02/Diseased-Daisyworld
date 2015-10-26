function startGraph

    startSteps = [26 26 26 26 26 26 26 26 26 26 26];

    infec = [0 0.1 0.2 0.3 0.4 0.5 0.6 0.7 0.8 0.9 1.0];

    plot(infec, startSteps);
    title('Start of Homeostasis');
    xlabel('Infectivity');
    ylabel('Number of steps');
end