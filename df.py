#%pip install pyyaml

import yaml

def load_yaml_file(filepath):
    with open(filepath, 'r') as file:
        return yaml.safe_load(file)

def has_ref_type(schema):
    schema_names_with_ref = []
    for property_name, property_schema in schema.items():
        if '$ref' in property_schema:
            schema_names_with_ref.append(property_name)
    return schema_names_with_ref


def navigate_to_schema(api_spec, path):
    path = path.split('/')[1:]
    targe_schema = api_spec
    for key in path:
        targe_schema = targe_schema[key]
    return targe_schema


def dereference(component):
    model_names = component.keys()
    #print(f'model_names: {model_names}')
    try:
        for model_name in model_names:
            model_schema = component[model_name]
            print(f'isinstance(model_schema, list): {model_schema}, {isinstance(model_schema, list)}')
            if isinstance(model_schema, dict) == False:
                print(f'{model_name} is a string')
                return component
               
            print(f'{model_name}: {model_schema}')
            model_properties = None
            if model_schema['type'] == 'object':
                model_properties = model_schema['properties']
                # Check if Model Properties has a reference to another Model
                type_name_with_ref_list = has_ref_type(model_properties)
                print(f'type_name_with_ref_list: {type_name_with_ref_list}')
                for type_name_with_ref in type_name_with_ref_list:
                    print(f'{type_name_with_ref} has a reference')
                    resolved_schema = navigate_to_schema(api_spec, model_properties[type_name_with_ref]['$ref'])
                    model_properties[type_name_with_ref] = dereference(resolved_schema)
            if model_schema['type'] == 'array':
                model_properties = model_schema['items']
                # Check if Model Properties has a reference to another Model
                if '$ref' in model_properties:
                    resolved_schema = navigate_to_schema(api_spec, model_properties['$ref'])
                    model_schema['items'] = dereference(resolved_schema)
    except Exception as e:
        print(f'component {component}')
        print(f'error {e}')
    return component



filepath = 'openapi.yaml'
api_spec = load_yaml_file(filepath)    
component = api_spec['components']['schemas']
result = dereference(component)
