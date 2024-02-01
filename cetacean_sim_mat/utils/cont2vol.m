function [V, shp, volpoints] = cont2vol(prob_cont)
%CONT2VOL calculate the volume of a probability contour.

%%  Calcualte the intersected volume
% probability contour
volpoints=[];
n=1;
for i=1:length(prob_cont)
    for j=1:5:360
        if (prob_cont(i,2)<=0)
            %now find x, y, z point. y is north.
            volpoints(n,3)=prob_cont(i,2);
            volpoints(n,1)=prob_cont(i,1)*sind(j);
            volpoints(n,2)=prob_cont(i,1)*cosd(j);
            n=n+1;
        end
    end
end

%fill in top.
%check if the sea surface intersects the shape.
[shallowd, index]=max(prob_cont(:,2));

%  fill in range bins at top. (sea surface)
for i=0.01:0.5:prob_cont(index,1)
    for j=1:5:360
        %now find x, y, z point. y is north.
        volpoints(n,3)=shallowd;
        volpoints(n,1)=i*sind(j);
        volpoints(n,2)=i*cosd(j);
        n=n+1;
    end
end

%     fill in bottom
[deepd, index]=min(prob_cont(:,2));

%  fill in range bins at top. (sea surface)
for i=0.01:0.5:prob_cont(index,1)
    for j=1:5:360
        %now find x, y, z point. y is north.
        volpoints(n,3)=deepd;
        volpoints(n,1)=i*sind(j);
        volpoints(n,2)=i*cosd(j);
        n=n+1;
    end
end

if (isempty(volpoints))
    V=0;
    shp=[];
    return;
end

% volpoints(n,3)=min(prob_cont(:,2));
% volpoints(n,1)=0;
% volpoints(n,2)=0;
% n=n+1;
%
% volpoints(n,3)=max(prob_cont(:,2));
% volpoints(n,1)=0;
% volpoints(n,2)=0;

% %now make sure there's a t the deepest point at range =0;
% [val, index] = min(abs(contour(:,1)));
% volpoints(n,3)=contour(index,2);
% volpoints(n,1)=0;
% volpoints(n,2)=0;

% k = boundary(volpoints,1);
% trisurf(k,volpoints(:,1),volpoints(:,2),volpoints(:,3),'Facecolor','red','FaceAlpha',0.1)
%
shp = alphaShape(volpoints,10); %alpha needs to be high
V=volume(shp);

end

