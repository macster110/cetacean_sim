function [binCenter] = binedges2bincenter(binEdges)
%BINEDGES2BINCENTER Converts bin edges to bin centers
%   [BINCENTER] = BINEDGES2BINCENTER(BINEDGES) converts evenly distirbuted
%   bin edges to bin centers so that N edges becomes N-1 centers. 
binsize=abs(binEdges(1)-binEdges(2));

binCenter=zeros(length(binEdges)-1,1);

for i=1:length(binEdges)-1
    binCenter(i)= binEdges(i)+binsize/2; 
end

end

