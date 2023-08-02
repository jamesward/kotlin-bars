# https://github.com/tadfisher/android-nixpkgs
#
# nix-channel --add https://tadfisher.github.io/android-nixpkgs android-nixpkgs
# nix-channel --update android-nixpkgs

{ pkgs ? import <nixpkgs> { } }:

with pkgs;

let
  android-nixpkgs = callPackage <android-nixpkgs> { };

  android-sdk = android-nixpkgs.sdk (sdkPkgs: with sdkPkgs; [
    cmdline-tools-latest
    build-tools-33-0-2
    platform-tools
    platforms-android-33
    emulator
  ]);

in
mkShell {
  buildInputs = [
#    android-studio
    android-sdk
    graalvm17-ce
  ];
}
