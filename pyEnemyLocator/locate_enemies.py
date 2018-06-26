
# coding: utf-8

import os
import sys
import cv2 as cv
import numpy as np

battlefield_path = sys.argv[1]

output_path = None if len(sys.argv) < 3 else sys.argv[2]

def draw_rect(positions, size, img):

    for p in positions:
        x, y = p[0], p[1]
        w, h = size
        top_left = x - w // 2, y - h // 2
        bottom_right = x + w // 2, y + h // 2
        cv.rectangle(img, top_left, bottom_right, 255, 4)


def show_img(img):
    from matplotlib import pyplot as plt

    plt.imshow(img, cmap = 'gray')
    plt.show()


def contains(point, points, threshold):
    for p in points:
        d = np.linalg.norm(p - point)
        if d < threshold:
            return True
    return False


def merge_positions(a, b, threshold):
    result = list(a)
    for p in b:
        if contains(p, result, threshold):
            continue
        result.append(p)
    
    return result


def findn(small, big, std_factor = 5):

    img = big
    template = small

    w, h = template.shape[::-1]

    method = cv.TM_CCOEFF_NORMED
    # Apply template Matching
    res = cv.matchTemplate(img,template,method)

    flattened = res.flatten()

    std, mean = np.std(flattened), np.mean(flattened)

    threshold = mean + std * std_factor
    loc = np.where(res > threshold)

    existing = []
    result = []
    radius = np.linalg.norm(np.array([w, h]))

    for top_left in zip(*loc[::-1]):
        pos = np.array(top_left)
        if contains(pos, existing, radius):
            continue
        existing.append(pos)
        centre = np.asarray([top_left[0] + w // 2, top_left[1] + h // 2])
        result.append(centre)
        
    return result

battlefield_img = cv.imread(battlefield_path, 0)
positions = []

enemies_dir = 'enemies'
for p in os.listdir(enemies_dir):
    enemy_path = os.path.join(enemies_dir, p)
    enemy_img = cv.imread(enemy_path, 0)
    
    enemy_size = enemy_img.shape[::-1]
    threshold = np.linalg.norm(enemy_size)
    
    positions_ = findn(enemy_img, battlefield_img.copy(), 4)

    positions = merge_positions(positions, positions_, threshold)

for p in positions:
    print(p[0], p[1], sep=", ")

draw_rect(positions, enemy_size, battlefield_img)
if output_path:
    cv.imwrite(output_path, battlefield_img)

