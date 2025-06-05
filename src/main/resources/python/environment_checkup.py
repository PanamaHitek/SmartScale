import os

files_to_check = ['retrieve_data.py', 'scan_ble.py']

def main():
    all_files_exist = True
    
    # Get the directory of the current script
    script_dir = os.path.dirname(os.path.abspath(__file__))
    
    for filename in files_to_check:
        # Create absolute path for each file
        file_path = os.path.join(script_dir, filename)
        if not os.path.exists(file_path):
            all_files_exist = False
            break
    
    if all_files_exist:
        return "OK"
    else:
        return "Not OK"

if __name__ == "__main__":
    print(main())