#!/bin/bash

# Get the number of changed files in the latest commit
changed_files=$(git diff --name-only HEAD~1 HEAD | wc -l)

# Get the current versionName from build.gradle.kts
current_version=$(grep "versionName =" app/build.gradle.kts | awk '{ print $3 }' | tr -d '"')

# Split the current versionName into its components
IFS='.' read -r -a version_parts <<< "$current_version"
MAJOR=${version_parts[0]}
MINOR=${version_parts[1]}
PATCH=${version_parts[2]}

# Determine the increment based on the number of changed files
if [ "$changed_files" -le 5 ]; then
  PATCH=$((PATCH + 1))
elif [ "$changed_files" -le 20 ]; then
  MINOR=$((MINOR + 1))
  PATCH=0
else
  MAJOR=$((MAJOR + 1))
  MINOR=0
  PATCH=0
fi

# Construct the new versionName
new_version="$MAJOR.$MINOR.$PATCH"

# Update the build.gradle.kts file with the new versionName
sed -i "s/versionName = \"$current_version\"/versionName = \"$new_version\"/" app/build.gradle.kts

echo "Updated versionName to $new_version"