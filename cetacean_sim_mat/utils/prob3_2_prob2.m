function [p2, p1, areacomp] = prob3_2_prob2(probdet, circarea)
%PROB32PROB2 Converts a probability of detection surface in 3D to a 2D
%range and prob. 
% [P2 P1] = PROB32PROB2(PROBDET) returns the 2D probability of detection
% with range P2 and the total mean probability of detection P1 from a 3D
% probability of detection PROBDET. PROBDET is a 3D probability of
% detection with p a surface with respect to both depth and range. The
% surfcae should contain fields 'range', 'depth' and 'p'. 

if nargin<2
   circarea=false; 
end

%iterate through each range bin
range3=probdet.range;
depth3=probdet.depth;
p3=probdet.p;

rangesbins=range3(1,:);
%add end bin;
rangesbins(end+1)=rangesbins(end)+(rangesbins(2)-rangesbins(1));

p2=zeros(length(range3(1,:)),2);  
for i=1:length(range3(1,:))
    totalp=0;
    %sum over depth
    for j=1:length(depth3(:,1))
        totalp=totalp+p3(j,i);
    end

    %work out the area compensation factor
    areacomp(i)=1;
    
    %% THIS DOES NOT WORK YET
%     if (circarea)
%        areacomp(i)=pi*(rangesbins(i+1)^2-rangesbins(i)^2);
%     end
    
    
    p2(i,1)=range3(1,i);
    p2(i,2)=totalp;
end

areacomp=areacomp/max(areacomp);

for i=1:length(p2(:,2))
    p2(i,2)=p2(i,2)*areacomp(i);
end


%p1 is simply the mean of the histogram. 
p1=sum(p2(:,2)); 
end

