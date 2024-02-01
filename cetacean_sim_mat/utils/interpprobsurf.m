function [histinterp] = interpprobsurf(probsurf, xbinedges, ybinedges)
%INTERP_HIST_SURF Interpolate a histogram surface 
%   [HISTINTERP] = INTERPPROBSURF(PROBSURF, XBINEDGES, YBINEDGES)
%   interpolates the probability surface generated by a Monte Carlo
%   simulation. PROBSURF is the probability of detection surface. XBINEDGES
%   are the edges of the x bins and YBINEDGES are the edges of the of the y
%   bins. XBINEDGES and YBINEDGES can aslo be center bins. HISTINTPER is a 
%   structure containing the interpolated surface and middle of the x bins. 

interpbin =0.1; %compared to bin of 1; 

if (length(xbinedges)~=length(probsurf(:,1)))
    %must make center bins
    xbins=xbinedges(1:length(xbinedges)-1)+((xbinedges(2)-xbinedges(1))/2);
    ybins=ybinedges(1:length(ybinedges)-1)+((ybinedges(2)-ybinedges(1))/2);
else
    %the inputs are the center bins
    xbins=xbinedges; 
    ybins=ybinedges; 
end


[X,Y]   = meshgrid(xbins, ybins);

%interpolate the plot a bit to make contours easier to analyse
[Xq,Yq]   = meshgrid(interp1(1:length(xbins),xbins, 1:interpbin:length(xbins)),...
    interp1(1:length(ybins), ybins, 1:interpbin:length(ybins)));

probdetinterp = interp2(X,Y,probsurf',Xq,Yq, 'cubic'); 

% the interp structure. 
histinterp.pq=probdetinterp;
histinterp.rangeq=Xq;
histinterp.depthq=Yq;

%also add data used to interpolate
histinterp.p=probsurf';
histinterp.range=X;
histinterp.depth=Y;



end
% 
