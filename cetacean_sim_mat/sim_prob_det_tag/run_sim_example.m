%% Load some tag data and simualte the probability of detection
% Uses real tag data to simulate the probability of detecting an animal on
% a static acoustic monitoring device at a defined depth.
%
% Note that this is essentially a MATLAB wrapper for a Java program called
% CetaceanSim
% https://github.com/macster110/cetacean_sim/tree/master/cetacean_sim. This
% organises the data, sends to CetaceanSim for processing and fetches the
% results. Things are done this way because MATLAB is an order of magnitude
% slower than Java at these types of processing tasks so directly calling
% java code greatly speeds up the simulation. 

%clear previous data and close figures
clear
close all

%% Set up the simulation
% load an example tag
tagdata2 = load('tagdata_example.mat');
tagdata=tagdata2.tagdata;
 
%create a settings structure to pass to the simualtion. 
settings = probtracksettings();
settings.gridSpacing = 200; % the spacing between sensors (less means more processing but more accurate results) 
settings.griddepth = -5; % the depth of the recievers. 

%CUE OR SNAPSHOT
settings.snap=true;  %true to use snapshot approach or false to calculate p for a single click
settings.snaptimebin=1; % the time period of a snapshot
day = 1; % 0 all, 1 darkness, 2 daylight


%% Filter tag data to remove buzzes and restrict to times whne tag was on animal
%buzz threshold
buzzici= 0.016; %we assume buzzes have an ici less than 16ms

%the beam profile
type = 'porp_measured'; 

%get rid if buzz clicks becuase we want only standard echolocation clicks.
ici = clks2ici(tagdata.clicks, tagdata.samplerate);

indexbzz = ici>buzzici;

%3/02/2022 -having the index around this way is really important as it means the
%echo is removed instead of the the main click if it has not been removed.
indexbzz = [indexbzz(1); indexbzz];

% remove the buzzes form the data
tagdata.clicks = tagdata.clicks(indexbzz, :);

timelims = tagdata.tagonoff;

%Make sure we are only considering the tag on off times...
%Note that this is very important because the simulation start time is
%referenced to the first track point - if the track starts beforte the
%simualtion then a different tagonff chunk will be used than expected...
index = tagdata.clicks(:,1)>timelims(1) & tagdata.clicks(:,1)<timelims(2);
tagdata.clicks =tagdata.clicks(index,:);


%% Run the simualation
%run the simulation
[probdet, effortdet, hydrophonearray, animalStruct] = simprobdettrackj(tagdata, settings);

%interpolate the probability surface
probdetq =  interpprobsurf(probdet.hist, probdet.xbinedges, probdet.ybinedges);


%% Plot the results
%Probability density function. th earea under this graph is the overall
%probability of detection. 
figure(1)
clf
hold on
%convert the 3D probability of detection to a 2D probability of detection.
[p2, p1, areacomp] = prob3_2_prob2(probdetq);
plot(p2(:,1), p2(:,2), 'LineWidth', 2)

xlabel('Range (m)')
ylabel('$$\hat{P}$$', 'Interpreter','Latex');

title({'Probability density function'},{['Total $$\hat{P}$$ is ' num2str(p1)]}, 'Interpreter','Latex');

%Detection function (i.e. compensate for the static devices detecting more
%animals at larger ranges because the monitored area is larger).
figure(2)
clf
E = sqrt((2*settings.maxrange)/(settings.maxrange^2)); 
detfunc =  E*p2(:,2)./(2*p2(:,1)/(settings.maxrange^2));
plot(p2(:,1),detfunc, 'LineWidth', 2)
title('Probability of detection function');

