function [probdetsettings] = probtracksettings()

%% Analysis area

%the maximum range that an animal can be detected at e.g. on axis source
%level - TL falls below threshold. 
probdetsettings.maxrange=1200; %meters

%the number of range bins
probdetsettings.rangebins=50; 

%the number of depth buns
probdetsettings.depthbins=20; 

%the maximum depth
probdetsettings.maxdepth=80; %meters

%the spacing between simulated sensors ona grid. 
probdetsettings.gridSpacing = 1000; %meters

%the depth of the grid - can be an array of depths for  3D grid
probdetsettings.griddepth = -5; %m
%% Detection and soundscape

%the minimum snr a detection must be before it is detected in the
%simulation
probdetsettings.snrThresh = 16; 

%the assumed noise. i..e a detection must be noise + SNR before it is
%detected
probdetsettings.noise = 90; 

%% Propogation

%the spreading coefficient for spherical spreading e.g. 20 for speherical
%and 10 for cylindrical. 
probdetsettings.spreadingCoeff = 20; 

%the absorption coefficient in dB/m 
probdetsettings.absorptionCoeff = 0.04; 

%% Simualtion tyoe

probdetsettings.snap = false ;%true to use snap approach to pdet

probdetsettings.snaptimebin = 1; %seconds - the time bin if using snap

end