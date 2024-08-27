#!/bin/bash

# Fetch the latest tag from GitHub
latest_tag=$(git describe --tags $(git rev-list --tags --max-count=1))

# Check if latest_tag is empty or null
if [ -z "$latest_tag" ]; then
  echo "No tags found. Setting default version 0.0.0."
  latest_tag="v0.0.0"
fi

# Remove the 'v' prefix if it exists
current_version=${latest_tag#v}

# Get the current versionCode from the root build.gradle.kts
current_version_code=$(grep "versionCode =" build.gradle.kts | awk '{ print $3 }')

# Check if current_version_code is empty or null
if [ -z "$current_version_code" ]; then
  echo "Error: current_version_code is empty or null. Setting default versionCode 1."
  current_version_code=1
fi

# Check if there are enough commits to perform the diff
if git rev-parse HEAD~1 >/dev/null 2>&1; then
  changed_files=$(git diff --name-only HEAD~1 HEAD | wc -l)
else
  echo "Not enough commits to perform diff, defaulting to 0 changed files."
  changed_files=0
fi

# Split the current versionName into its components
IFS='.' read -r -a version_parts <<< "$current_version"
MAJOR=${version_parts[0]:-0}
MINOR=${version_parts[1]:-0}
PATCH=${version_parts[2]:-0}

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

# Increment the versionCode by 1
new_version_code=$((current_version_code + 1))

# Construct the new versionName
new_version="$MAJOR.$MINOR.$PATCH"

# Update the root build.gradle.kts file with the new versionName and versionCode
sed -i "s/versionName = \"$current_version\"/versionName = \"$new_version\"/" build.gradle.kts
sed -i "s/versionCode = $current_version_code/versionCode = $new_version_code/" build.gradle.kts

echo "Updated versionName to $new_version and versionCode to $new_version_code"