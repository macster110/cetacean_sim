function [xbinedges, ybinedges] = xybinedges(xsurf,ysurf)
%XYBINEDGES Gets the bin edges from prob det. surface
%   [XBINEDGES, YBINEDGES] = XYBINEDGES(XSURF,YSURF) Calculates the x and y
%   bin edges from a probability of detection surface.

%get the x values.
xvals=xsurf(1,:);
xbinsize=xvals(2)-xvals(1);
xbinedges=xvals-xbinsize/2;
xbinedges(length(xbinedges)+1)=xbinedges(length(xbinedges))+xbinsize;

yvals=ysurf(:,1);
ybinsize=yvals(2)-yvals(1);
ybinedges=yvals-ybinsize/2;
ybinedges(length(ybinedges)+1)=ybinedges(length(ybinedges))+ybinsize;

ybinedges= ybinedges'; 

end

