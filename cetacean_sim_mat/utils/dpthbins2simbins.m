function [simbins] = dpthbins2simbins(depthbins)
%DPTHBINS2SIMBINS Converts depth bins to depth bins in correct format for
%a prob. det. simulation.
%   SIMBINS = DPTHBINS2SIMBINS(DEPTHBINS )Converts DEPTHBINS in the format
%   [max depth depth 1 depth 2 min depth] were depth is negative to the sim
%   format, SIMBINS, which is [[depthbin1]; [depthbin2]; ]

simbins=zeros(length(depthbins-1),2); 
for i=1:length(depthbins)-1
    simbins(i,1)=depthbins(i); 
    simbins(i,2)=depthbins(i+1); 
end

