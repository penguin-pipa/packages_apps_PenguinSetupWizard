#!/usr/bin/env python3
# SPDX-FileCopyrightText: The PenguinOS Project
# SPDX-License-Identifier: Apache-2.0

"""Generates the PenguinSetupWizard Lottie illustrations into res/raw: python3 illustrations.py res/raw"""
import json, sys
from lottie import objects, NVector, Color
from lottie.objects import easing
from lottie.parsers.svg.importer import PathDParser
from lottie.exporters.core import export_lottie

OUT = sys.argv[1]
INK = Color(0.1, 0.1, 0.1)
ACCENT = Color(0.91, 0.45, 0.2)


def kf(prop, frames, ease=None):
    for t, v in frames:
        prop.add_keyframe(t, v, ease or easing.Sigmoid())


def anim(w, h, frames, fps=60):
    a = objects.Animation(frames, fps)
    a.width, a.height = w, h
    return a


def path_shape(d):
    p = PathDParser(d)
    p.parse()
    return [objects.Path(b) for b in p.paths]


def welcome():
    logo_body = ("M 163.688 289.604 C 168.393 294.977 161 299.006 161 299.006 C 161 299.006 163.688 303.036 169.065 301.021 "
                 "C 169.065 301.021 183.852 290.947 255.768 296.992 C 255.768 296.992 309.537 303.708 308.193 290.275 "
                 "C 308.193 290.275 314.914 282.216 269.21 257.366 C 269.21 257.366 224.851 216.397 330.373 204.979 "
                 "C 330.373 204.979 451.353 194.233 378.093 319.827 C 378.093 319.827 310.209 376.915 323.651 389.676 "
                 "L 415.731 526.015 C 415.731 526.015 426.485 532.731 426.485 526.015 L 440.599 457.51 L 418.419 359.453 "
                 "C 418.419 359.453 491.008 337.961 482.27 204.979 C 482.27 204.979 462.779 57.894 273.915 139.832 "
                 "C 273.915 139.832 201.327 174.084 220.146 231.172 C 220.146 231.172 158.984 284.231 163.688 289.604 Z")
    a = anim(644, 644, 150)
    layer = objects.ShapeLayer()
    layer.name = "logo"
    a.add_layer(layer)
    layer.transform.anchor_point.value = NVector(322, 322)
    layer.transform.position.value = NVector(322, 322)
    kf(layer.transform.scale, [(0, NVector(88, 88)), (110, NVector(100, 100))])

    eye = layer.add_shape(objects.Group())
    eye.name = "eye"
    eye.add_shape(objects.Ellipse(NVector(317.6, 232.5), NVector(57.8, 12.1)))
    eye.add_shape(objects.Fill(ACCENT)).name = "accent eye"
    eye.transform.anchor_point.value = NVector(317.6, 232.5)
    eye.transform.position.value = NVector(317.6, 232.5)
    kf(eye.transform.scale, [(84, NVector(0, 0)), (100, NVector(118, 118)), (114, NVector(100, 100))])

    body = layer.add_shape(objects.Group())
    body.name = "body"
    for s in path_shape(logo_body):
        body.add_shape(s)
    trim = objects.Trim()
    kf(trim.end, [(0, 0), (72, 100)])
    stroke = objects.Stroke(ACCENT, 9)
    stroke.name = "accent outline"
    stroke.line_cap = objects.LineCap.Round
    stroke.line_join = objects.LineJoin.Round
    kf(stroke.opacity, [(70, 100), (100, 0)])
    fill = objects.Fill(ACCENT)
    fill.name = "accent body"
    kf(fill.opacity, [(56, 0), (96, 100)])
    body.add_shape(stroke)
    body.add_shape(trim)
    body.add_shape(fill)
    return a


def smile():
    a = anim(400, 400, 110)
    layer = objects.ShapeLayer()
    layer.name = "smile"
    a.add_layer(layer)
    layer.transform.anchor_point.value = NVector(200, 200)
    kf(layer.transform.position, [(10, NVector(200, 214)), (48, NVector(200, 200))])

    for name, x in (("left eye", 140), ("right eye", 260)):
        eye = layer.add_shape(objects.Group())
        eye.name = name
        eye.add_shape(objects.Ellipse(NVector(x, 150), NVector(34, 50)))
        eye.add_shape(objects.Fill(INK)).name = "ink eye"
        eye.transform.anchor_point.value = NVector(x, 150)
        eye.transform.position.value = NVector(x, 150)
        kf(eye.transform.scale, [(0, NVector(100, 0)), (14, NVector(100, 112)), (22, NVector(100, 100)),
                                 (78, NVector(100, 100)), (83, NVector(100, 10)), (88, NVector(100, 100))])

    mouth = layer.add_shape(objects.Group())
    mouth.name = "mouth"
    for s in path_shape("M 104 236 C 140 316 260 316 296 236"):
        mouth.add_shape(s)
    trim = objects.Trim()
    kf(trim.end, [(14, 0), (50, 100)])
    stroke = objects.Stroke(ACCENT, 30)
    stroke.name = "accent mouth"
    kf(stroke.opacity, [(13, 0), (14, 100)], easing.Jump())
    stroke.line_cap = objects.LineCap.Round
    mouth.add_shape(stroke)
    mouth.add_shape(trim)
    return a


def sim():
    a = anim(400, 400, 150)
    phone = objects.ShapeLayer()
    phone.name = "phone"
    a.add_layer(phone)
    body = phone.add_shape(objects.Group())
    body.name = "phone body"
    body.add_shape(objects.Rect(NVector(262, 200), NVector(150, 300), 26))
    body.add_shape(objects.Stroke(INK, 10)).name = "ink phone"
    slot = phone.add_shape(objects.Group())
    slot.name = "slot"
    slot.add_shape(objects.Rect(NVector(187, 150), NVector(12, 70), 6))
    slot.add_shape(objects.Fill(INK)).name = "ink slot"

    card = objects.ShapeLayer()
    card.name = "card"
    a.add_layer(card)
    card.transform.anchor_point.value = NVector(0, 0)
    kf(card.transform.position, [(24, NVector(0, 0)), (80, NVector(64, 0))])
    kf(card.transform.opacity, [(0, 0), (16, 100), (96, 100), (110, 0)], easing.Linear())
    g = card.add_shape(objects.Group())
    g.name = "card body"
    for s in path_shape("M 70 122 L 108 122 L 124 138 L 124 178 Q 124 186 116 186 L 70 186 Q 62 186 62 178 L 62 130 Q 62 122 70 122 Z"):
        g.add_shape(s)
    g.add_shape(objects.Fill(ACCENT)).name = "accent card"
    chip = card.add_shape(objects.Group())
    chip.name = "chip"
    chip.add_shape(objects.Rect(NVector(92, 160), NVector(26, 20), 4))
    chip.add_shape(objects.Fill(Color(1, 1, 1))).name = "chip fill"
    return a


for name, make in (("welcome_logo", welcome), ("finish_smile", smile), ("sim_insert", sim)):
    with open(f"{OUT}/{name}.json", "w") as f:
        export_lottie(make(), f)
    print("wrote", name)
