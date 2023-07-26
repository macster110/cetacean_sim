function dist = diststraight(pos1, pos2)
dist = sqrt((pos1(1)-pos2(1))^2+(pos1(2)-pos2(2))^2+(pos1(3)-pos2(3))^2);
% dist=pdist([pos1; pos2],'euclidean');

return
end
