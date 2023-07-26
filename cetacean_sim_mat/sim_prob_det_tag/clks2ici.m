function [ici] = clks2ici(clks, sR)
%CLKS2ICI Convert clk structure to ICI list
% [ICI] = CLKS2ICI(CLKS, SR) converts a struct array of CLKS to an array of
% inter-click-interval (ICI) values. ICI is calculated using the sample
% number of the clicksand if that is not available then the milliseconds
% time stamp.

% size(clks)

ici_num = diff(clks(:,1))*60*60*24;

ici = diff(clks(:,2))./sR;

for i=1:length(ici)
    if (ici(i)<0)
        ici(i)=ici_num(i);
    elseif  (ici(i)<0.005) %5ms
        %need finer resolution than sample start time
        samplepeakdiff = clks(i+1,7)-clks(i,7);
        ici(i) = ici(i)+samplepeakdiff/sR; 
    end
end


end

